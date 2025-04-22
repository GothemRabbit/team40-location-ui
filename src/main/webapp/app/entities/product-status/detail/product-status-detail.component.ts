import { Component, input, signal, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule, NgOptimizedImage } from '@angular/common';
import { FormsModule } from '@angular/forms';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IProductStatus } from '../product-status.model';
import { ProductStatusService } from '../service/product-status.service';

import { ItemService } from 'app/entities/item/service/item.service';
import { IItem } from 'app/entities/item/item.model';
import { ImagesService } from 'app/entities/images/service/images.service';
import { IImages } from '../../images/images.model';

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

  showModal = false;
  rating: number | null = null;
  comment = '';

  showConfirmModal = false;
  confirmActionType: 'confirm' | 'cancel' | null = null;

  constructor(
    private productStatusService: ProductStatusService,
    private itemService: ItemService,
    private imagesService: ImagesService,
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

  showConfirmDialog(action: 'confirm' | 'cancel'): void {
    this.confirmActionType = action;
    this.showConfirmModal = true;
  }

  closeConfirmDialog(): void {
    this.showConfirmModal = false;
    this.confirmActionType = null;
  }
  getImageSrc(imageData: string | null | undefined): string {
    if (!imageData) return 'assets/images/placeholder.png'; // 如果没有图片数据，返回占位符图片

    // 将字节数组转换为 Base64 格式
    const base64Data = this.convertByteArrayToBase64(imageData);

    // 返回 Base64 数据源
    return `data:image/png;base64,${base64Data}`;
  }

  // 将字节数组（如 "[B@xxxxxx"）转换为 Base64 字符串
  convertByteArrayToBase64(byteArrayStr: string): string {
    const byteArray = new TextEncoder().encode(byteArrayStr); // 转换为 Uint8Array
    let binaryString = '';
    // eslint-disable-next-line @typescript-eslint/prefer-for-of
    for (let i = 0; i < byteArray.length; i++) {
      binaryString += String.fromCharCode(byteArray[i]);
    }
    return btoa(binaryString); // 使用 btoa 转换为 Base64 字符串
  }

  executeAction(): void {
    if (this.confirmActionType === 'confirm') {
      this.onConfirm();
    } else if (this.confirmActionType === 'cancel') {
      this.onCancel();
    }
    this.closeConfirmDialog();
    this.previousState();
  }

  onConfirm(): void {
    const current = this.productStatus();
    if (!current) return;

    this.productStatusService.partialUpdate({ id: current.id, status: 'COMPLETED' }).subscribe();
  }

  onCancel(): void {
    const current = this.productStatus();
    if (!current) return;

    this.productStatusService.partialUpdate({ id: current.id, status: 'CANCELLED' }).subscribe();
  }
}
