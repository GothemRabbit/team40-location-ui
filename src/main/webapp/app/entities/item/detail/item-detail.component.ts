import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, RouterModule, Router } from '@angular/router';
import dayjs from 'dayjs';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { DataUtils } from 'app/core/util/data-util.service';
import { IItem } from '../item.model';
import { ImagesComponent } from '../../images/list/images.component';
import { ItemService } from '../service/item.service';
import { IProfileDetails } from '../../profile-details/profile-details.model';
import { filter, take, tap } from 'rxjs';
import { LoginService } from 'app/login/login.service';
import { LikesService } from '../../likes/service/likes.service';
import { ItemDeleteDialogComponent } from '../delete/item-delete-dialog.component';
import { ITEM_DELETED_EVENT } from '../../../config/navigation.constants';
import { ConversationService } from '../../conversation/service/conversation.service';
import { type NewConversation } from '../../conversation/conversation.model';

@Component({
  standalone: true,
  selector: 'jhi-item-detail',
  templateUrl: './item-detail.component.html',
  styleUrl: 'item-detail.component.scss',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe, ImagesComponent],
})
export class ItemDetailComponent implements OnInit {
  item = signal<IItem | null>(null);
  imageUrls: string[] = [];
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
  private convoService = inject(ConversationService);
  private router = inject(Router);

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
        // this.item.update(() => response.body);
        // if (this.item()?.images?.length) {
        //   this.currentSlideIndex = 0;
        // }
        const body = response.body!;
        this.item.set(body);

        if (body.images?.length) {
          // build an array of data-URLs for the slideshow
          this.imageUrls = body.images.map(img => `data:${img.imagesContentType ?? 'image/png'};base64,${img.images ?? ''}`);
        } else {
          // no images on the DTO → show exactly one placeholder
          this.imageUrls = ['content/images/placeholder.png'];
        }
        this.currentSlideIndex = 0;

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
    // const images = this.item()?.images;
    // if (images && images.length > 0) {
    //   this.currentSlideIndex = (this.currentSlideIndex + 1) % images.length;
    // }
    if (this.imageUrls.length > 0) {
      this.currentSlideIndex = (this.currentSlideIndex + 1) % this.imageUrls.length;
    }
  }

  prevSlide(): void {
    // const images = this.item()?.images;
    // if (images && images.length > 0) {
    //   this.currentSlideIndex = (this.currentSlideIndex - 1 + images.length) % images.length;
    // }
    if (this.imageUrls.length > 0) {
      this.currentSlideIndex = (this.currentSlideIndex - 1 + this.imageUrls.length) % this.imageUrls.length;
    }
  }

  goToSlide(index: number): void {
    // const images = this.item()?.images;
    // if (images && index >= 0 && index < images.length) {
    //   this.currentSlideIndex = index;
    // }
    if (index >= 0 && index < this.imageUrls.length) {
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

  messageSeller(): void {
    const sellerProfileId = this.item()?.profileDetails?.id;
    if (!sellerProfileId) {
      console.error('Seller ID missing');
      return;
    }

    this.loginService
      .getProfileDetails()
      .pipe(take(1))
      .subscribe(myPd => {
        if (!myPd) {
          console.error('User must be logged in to message');
          return;
        }

        const conv: NewConversation = {
          id: null,
          dateCreated: dayjs(),
          profileDetails: [{ id: sellerProfileId }],
          participants: [{ id: myPd.id }],
        } as NewConversation;

        this.convoService.create(conv).subscribe({
          next: res => {
            const newId = res.body?.id;
            if (newId) {
              this.router.navigate(['/conversation', newId, 'view']);
            } else {
              this.router.navigate(['/conversation']);
            }
          },
          error: err => {
            this.convoService.fetchMyVibes().subscribe(listRes => {
              const convId = listRes.body?.find(c => {
                const hasSeller = c.profileDetails?.some(pd => pd.id === sellerProfileId);
                const hasMe = c.participants?.some(pd => pd.id === myPd.id);
                return hasSeller && hasMe;
              })?.id;
              if (convId) {
                this.router.navigate(['/conversation', convId, 'view']);
              } else {
                this.router.navigate(['/conversation']);
              }
            });
          },
        });
      });
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
