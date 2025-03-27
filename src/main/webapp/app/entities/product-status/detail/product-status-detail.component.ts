import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IProductStatus } from '../product-status.model';
import { ProductStatusService } from '../service/product-status.service';

@Component({
  standalone: true,
  selector: 'jhi-product-status-detail',
  templateUrl: './product-status-detail.component.html',
  styleUrls: ['../product-status.styles.css'],
  imports: [SharedModule, RouterModule, CommonModule, FormsModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ProductStatusDetailComponent {
  productStatus = input<IProductStatus | null>(null);

  showModal = false;
  rating: number | null = null;
  comment = '';

  // 确认对话框相关属性
  showConfirmModal = false;
  confirmActionType: 'confirm' | 'cancel' | null = null;

  constructor(private productStatusService: ProductStatusService) {}

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
    if (!current) {
      return;
    }

    this.productStatusService.partialUpdate({ id: current.id, status: 'COMPLETED' }).subscribe({
      next(response) {
        if (response.body) {
          // @ts-expect-error: 可调用111111111111
          current.set(response.body);
        }
      },
      error(error) {
        console.error('Confirm wrong:', error);
      },
    });
  }

  onCancel(): void {
    const current = this.productStatus();
    if (!current) {
      return;
    }

    this.productStatusService.partialUpdate({ id: current.id, status: 'CANCELLED' }).subscribe({
      next(response) {
        if (response.body) {
          // @ts-expect-error: 可调用222222222222
          current.set(response.body);
        }
      },
      error(error) {
        console.error('Cancel wrong:', error);
      },
    });
  }
}
