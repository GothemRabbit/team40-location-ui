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
  likesCount = 0;
  profileDetails: IProfileDetails | undefined;
  profileLoaded = signal(false);

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
        this.item.update(() => response.body); // Correctly updating InputSignal
        if (this.item()?.images?.length) {
          this.currentSlideIndex = 0;
        }
      },
      error: err => console.error('Error fetching item:', err),
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

  // Fetch likes count for the item
  loadLikesCount(itemId: number): void {
    this.itemService.getLikesCount(itemId).subscribe({
      next: count => {
        this.likesCount = count; // Set the likes count
      },
      error: err => console.error('Error fetching like count:', err),
    });
  }

  // Toggle like/unlike the item
  // toggleLike(): void {
  //   const itemId = this.item()?.id;
  //   const profileId = this.profileDetails?.id;
  //
  //   if (!itemId || !profileId) {
  //     console.error('Missing itemId or profileId');
  //     return;
  //   }
  //
  //   const currentItem = this.item()!;
  //
  //   // Optimistic UI update
  //   const isLiked = !currentItem.isLikedByUser;
  //   const updatedLikes = isLiked ? (currentItem.likesCount ?? 0) + 1 : Math.max(0, (currentItem.likesCount ?? 0) - 1);
  //
  //   this.item.update(() => ({
  //     ...currentItem,
  //     isLikedByUser: isLiked,
  //     likesCount: updatedLikes,
  //   }));
  //
  //   this.itemService.toggleLike(itemId, profileId).subscribe({
  //     next: updatedLike => {
  //       this.item.update(() => ({
  //         ...currentItem,
  //         isLikedByUser: updatedLike.liked,
  //         likesCount: updatedLike.likesCount,
  //       }));
  //     },
  //     error: err => {
  //       console.error('Error toggling like:', err);
  //       this.item.update(() => currentItem); // Rollback
  //     },
  //   });
  // }

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
            // Optionally redirect to login or show a message
          }
        },
        error(err) {
          console.error('Error fetching profile details:', err);
        },
      });
  }
}
