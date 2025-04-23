import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import dayjs from 'dayjs';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { DataUtils } from 'app/core/util/data-util.service';
import { IItem } from '../item.model';
import { ImagesComponent } from '../../images/list/images.component';
import { ItemService } from '../service/item.service';
import { IProfileDetails } from '../../profile-details/profile-details.model';
import { take } from 'rxjs';
import { LoginService } from 'app/login/login.service';
import { LikesService } from '../../likes/service/likes.service';

@Component({
  standalone: true,
  selector: 'jhi-item-detail',
  templateUrl: './item-detail.component.html',
  styleUrl: 'item-detail.component.scss',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe, ImagesComponent],
})
export class ItemDetailComponent implements OnInit {
  item = signal<IItem | null>(null);
  currentSlideIndex = 0;
  profileDetails: IProfileDetails | undefined;
  profileLoaded = signal(false);
  likesCount = signal(0);
  isLiked = signal(false);
  likeMessage = signal<string | null>(null);

  protected loginService = inject(LoginService);
  protected dataUtils = inject(DataUtils);
  private route = inject(ActivatedRoute);
  private itemService = inject(ItemService);
  private likesService = inject(LikesService);

  ngOnInit(): void {
    // Fetch profile details first
    this.fetchProfileDetails();
    this.route.params.subscribe(params => {
      const itemId = params['id'];
      if (itemId) {
        this.loadItem(itemId);
      }
    });
  }

  loadItem(itemId: number): void {
    this.itemService.find(itemId).subscribe({
      next: response => {
        this.item.update(() => response.body);
        if (this.item()?.images?.length) {
          this.currentSlideIndex = 0;
        }

        // ✨ Load likes info after item and profileDetails are loaded
        if (this.profileDetails) {
          this.loadLikesData(itemId, this.profileDetails.id);
        }
      },
      error: err => console.error('Error fetching item:', err),
    });
  }

  toggleLike(): void {
    const itemId = this.item()?.id;
    const profileId = this.profileDetails?.id;
    if (!itemId || !profileId) return;

    // Optimistic update
    const previouslyLiked = this.isLiked();
    this.isLiked.set(!previouslyLiked);
    this.likesCount.update(count => (previouslyLiked ? count - 1 : count + 1));

    this.likesService.toggleLike(itemId, profileId).subscribe({
      error: err => {
        console.error('Toggle like failed:', err);
        // Revert optimistic update
        this.isLiked.set(previouslyLiked);
        this.likesCount.update(count => (previouslyLiked ? count + 1 : count - 1));
      },
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
  reserveItem(): void {
    this.loginService
      .getProfileDetails()
      .pipe(take(1))
      .subscribe((profileDetails: IProfileDetails | undefined) => {
        if (profileDetails) {
          const buyerProfileId = profileDetails.id;
          const itemId = this.item()?.id;

          if (!itemId) {
            console.error('Item ID is missing');
            return;
          }
          this.itemService.reserveItemInProductStatus(itemId, buyerProfileId).subscribe();
        }
      });
  }

  isOwner(): boolean {
    return this.profileDetails?.id === this.item()?.profileDetails?.id;
  }

  nextSlide(): void {
    const images = this.item()?.images;
    if (images && images.length > 0) {
      this.currentSlideIndex = (this.currentSlideIndex + 1) % images.length;
    }
  }

  prevSlide(): void {
    const images = this.item()?.images;
    if (images && images.length > 0) {
      this.currentSlideIndex = (this.currentSlideIndex - 1 + images.length) % images.length;
    }
  }

  goToSlide(index: number): void {
    const images = this.item()?.images;
    if (images && index >= 0 && index < images.length) {
      this.currentSlideIndex = index;
    }
  }

  getDaysSinceListed(timeListed: dayjs.Dayjs | null | undefined): string {
    if (!timeListed) return 'Unknown';

    const listedDate = dayjs(timeListed);
    const today = dayjs();
    const diffDays = today.diff(listedDate, 'day');

    return diffDays === 0 ? 'Listed today' : `Listed ${diffDays} day(s) ago`;
  }

  // Fetch profile details from the LoginService
  private fetchProfileDetails(): void {
    this.loginService
      .getProfileDetails()
      .pipe(take(1))
      .subscribe({
        next: (profileDetails: IProfileDetails | undefined) => {
          if (profileDetails) {
            this.profileDetails = profileDetails;
            this.profileLoaded.set(true);
          } else {
            console.error('No profile details found');
            this.profileLoaded.set(false);
            this.likeMessage.set('Please sign in to like item');
          }
        },
        error(err) {
          console.error('Error fetching profile details:', err);
        },
      });
  }

  private loadLikesData(itemId: number, profileId: number): void {
    this.likesService.getLikeCount(itemId).subscribe(count => this.likesCount.set(count));
    this.likesService.checkIfLiked(itemId, profileId).subscribe(isLiked => this.isLiked.set(isLiked));
  }
}
