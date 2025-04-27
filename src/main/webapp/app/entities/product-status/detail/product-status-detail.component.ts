import { Component, input, signal, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule, NgOptimizedImage } from '@angular/common';
import { FormsModule } from '@angular/forms';
import dayjs from 'dayjs/esm';
import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IProductStatus } from '../product-status.model';
import { ProductStatusService } from '../service/product-status.service';

import { ItemService } from 'app/entities/item/service/item.service';
import { IItem } from 'app/entities/item/item.model';
import { ImagesService } from 'app/entities/images/service/images.service';
import { IImages } from '../../images/images.model';
import { ProfileDetailsService } from '../../profile-details/service/profile-details.service';

@Component({
  standalone: true,
  selector: 'jhi-product-status-detail',
  templateUrl: './product-status-detail.component.html',
  styleUrls: ['../product-status.styles.css'],
  imports: [SharedModule, RouterModule, CommonModule, FormsModule, FormatMediumDatetimePipe, NgOptimizedImage],
})
export class ProductStatusDetailComponent implements OnInit {
  productStatus = input<IProductStatus | null>(null);
  fullItem = signal<IItem | null>(null);
  itemImages: IImages[] = [];
  editingTime = false;
  newMeetingTime: string | null = null;

  showModal = false;
  rating: number | null = null;
  comment = '';
  isSeller = false;
  isBuyer = false;
  showConfirmModal = false;
  confirmActionType: 'confirm' | 'cancel' | 'Cconfirm' | null = null;

  constructor(
    private productStatusService: ProductStatusService,
    private itemService: ItemService,
    private imagesService: ImagesService,
    private profileDetailsService: ProfileDetailsService,
  ) {}

  ngOnInit(): void {
    const ps = this.productStatus();
    if (ps?.item?.id) {
      this.itemService.find(ps.item.id).subscribe(r => {
        this.fullItem.set(r.body);
      });
    }
    if (ps?.item?.id) {
      this.imagesService.findAllByItemId(ps.item.id).subscribe(response => {
        this.itemImages = response.body ?? [];
      });
    }
    this.profileDetailsService.getCurrentUserProfile().subscribe(profileDetailsDTO => {
      const currentProfileId = profileDetailsDTO.id;
      this.isSeller = ps?.profileDetails?.id === currentProfileId;
      this.isBuyer = ps?.profileDetails1?.id === currentProfileId;
    });
  }
  startEditTime(): void {
    const current = this.productStatus();
    if (!current) return;
    this.newMeetingTime = current.meetingTime ? current.meetingTime.format('YYYY-MM-DDTHH:mm') : null;
    this.editingTime = true;
  }

  cancelEditTime(): void {
    this.editingTime = false;
    this.newMeetingTime = null;
  }

  saveEditTime(): void {
    const current = this.productStatus();
    if (!current || !this.newMeetingTime) {
      this.cancelEditTime();
      return;
    }

    this.productStatusService
      .partialUpdate({
        id: current.id,
        meetingTime: dayjs(this.newMeetingTime),
      })
      .subscribe({
        next: () => {
          current.meetingTime = dayjs(this.newMeetingTime);
          this.cancelEditTime();
        },
        error: () => alert('Update failed, please try again.'),
      });
  }

  previousState(): void {
    window.history.back();
  }
  openReviewModal(): void {
    this.showModal = true;
    this.rating = null;
    this.comment = '';
  }

  closeReviewModal(event?: MouseEvent): void {
    if (!event || (event.target as HTMLElement).classList.contains('modal')) {
      this.showModal = false;
      this.rating = null;
      this.comment = '';
    }
  }

  stopPropagation(event: MouseEvent): void {
    event.stopPropagation();
  }

  setRating(star: number): void {
    this.rating = star;
  }

  submitReview(): void {
    if (this.rating === null) {
      alert('Please select a rating before submitting.');
      return;
    }
    const current = this.productStatus();
    if (!current) {
      alert('No product status available to review.');
      return;
    }
    this.closeReviewModal();
  }

  showConfirmDialog(action: 'confirm' | 'cancel' | 'Cconfirm'): void {
    this.confirmActionType = action;
    this.showConfirmModal = true;
  }

  closeConfirmDialog(): void {
    this.showConfirmModal = false;
    this.confirmActionType = null;
  }
  getImageSrc(imageData: string | null | undefined): string {
    if (!imageData) return 'content/images/placeholder.png';
    const base64Data = this.convertByteArrayToBase64(imageData);
    return `data:image/png;base64,${base64Data}`;
  }
  convertByteArrayToBase64(byteArrayStr: string): string {
    const byteArray = new TextEncoder().encode(byteArrayStr);
    let binaryString = '';
    // eslint-disable-next-line @typescript-eslint/prefer-for-of
    for (let i = 0; i < byteArray.length; i++) {
      binaryString += String.fromCharCode(byteArray[i]);
    }
    return btoa(binaryString);
  }

  executeAction(): void {
    if (this.confirmActionType === 'confirm') {
      this.onConfirm();
    } else if (this.confirmActionType === 'cancel') {
      this.onCancel();
    } else if (this.confirmActionType === 'Cconfirm') {
      this.CConfirm();
    }
    this.closeConfirmDialog();
    this.previousState();
  }

  onConfirm(): void {
    const current = this.productStatus();
    if (!current) return;
    this.productStatusService.partialUpdate({ id: current.id, status: 'COMPLETED' }).subscribe();
  }
  CConfirm(): void {
    const current = this.productStatus();
    if (!current) return;
    this.productStatusService.partialUpdate({ id: current.id, status: 'RESERVED' }).subscribe();
  }

  onCancel(): void {
    const current = this.productStatus();
    if (!current) return;

    this.productStatusService.partialUpdate({ id: current.id, status: 'CANCELLED' }).subscribe();
  }
}
