import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { DataUtils } from 'app/core/util/data-util.service';
import { IItem } from '../item.model';
import { ImagesComponent } from '../../images/list/images.component';
import { ItemService } from '../service/item.service';

@Component({
  standalone: true,
  selector: 'jhi-item-detail',
  templateUrl: './item-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe, ImagesComponent],
})
export class ItemDetailComponent implements OnInit {
  // item = input<IItem | null>(null);
  item = signal<IItem | null>(null);
  // isLikedByUser = computed(() => this.item()?.isLikedByUser ?? false);
  currentSlideIndex = 0;

  protected dataUtils = inject(DataUtils);
  private route = inject(ActivatedRoute);
  private itemService = inject(ItemService);

  // constructor(
  //   private route: ActivatedRoute,
  //   private itemService: ItemService,
  //   private dataUtils = inject(DataUtils)
  // ) {}

  ngOnInit(): void {
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

  toggleLike(): void {
    const currentItem = this.item();
    if (!currentItem) return;

    // Ensure `likesCount` is a valid number
    // const currentLikes = currentItem.likesCount ?? 0;
    const currentLikes = Number(currentItem.likesCount) || 0;
    const isLiked = !currentItem.isLikedByUser;
    const updatedLikes = isLiked ? currentLikes + 1 : Math.max(0, currentLikes - 1); // Avoid negative values

    // Optimistically update UI
    this.item.update(() => ({
      ...currentItem,
      isLikedByUser: isLiked,
      likesCount: updatedLikes,
    }));

    // Send API request
    this.itemService.likeItem(currentItem.id).subscribe({
      next: response => {
        if (response.body) {
          this.item.update(() => response.body as IItem);
        }
      },
      error: err => {
        console.error('Error toggling like:', err);
        this.item.update(() => currentItem); // Revert UI if API call fails
      },
    });
  }

  // hasUserLiked(): boolean {
  //   return this.item()?.isLikedByUser ?? false;
  // }
  hasUserLiked(): boolean {
    return !!this.item()?.isLikedByUser;
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
}
