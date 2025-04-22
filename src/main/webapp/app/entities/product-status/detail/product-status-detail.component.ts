import { Component, input, signal, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule, NgOptimizedImage } from '@angular/common';
import { FormsModule } from '@angular/forms';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date'; // ★ DurationPipe / FormatMediumDatePipe 移除
import { IProductStatus } from '../product-status.model';
import { EntityArrayResponseType, ProductStatusService } from '../service/product-status.service';
import { ImagesService } from 'app/entities/images/service/images.service';

import { ItemService } from 'app/entities/item/service/item.service';
import { IItem } from 'app/entities/item/item.model';
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
      // 使用 ImagesService 获取与商品关联的图片
      this.imagesService.findAllByItemId(ps.item.id).subscribe(response => {
        this.itemImages = response.body ?? []; // 确保从响应中提取数组部分
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
    if (!imageData) return 'assets/images/placeholder.png';
    const base64Data = this.convertByteArrayToBase64(imageData);
    return `data:image/png;base64,${base64Data}`;
  }
  convertByteArrayToBase64(byteArrayStr: string): string {
    const byteArray = new TextEncoder().encode(byteArrayStr); // 转换为 Uint8Array
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
