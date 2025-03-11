import { Component, computed, inject, input, signal } from '@angular/core';
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
export class ItemDetailComponent {
  //item = input<IItem | null>(null);
  item = signal<IItem | null>(null);
  //isLikedByUser = computed(() => this.item()?.isLikedByUser ?? false);

  protected dataUtils = inject(DataUtils);

  constructor(
    private route: ActivatedRoute,
    private itemService: ItemService,
  ) {}

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
        this.item.update(() => response.body); // ✅ Correctly updating InputSignal
      },
      error: err => console.error('Error fetching item:', err),
    });
  }

  hasUserLiked(): boolean {
    return this.item()?.isLikedByUser ?? false;
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
    if (this.item()) {
      this.itemService.likeItem(this.item()!.id).subscribe({
        next: response => {
          this.item.update(() => response.body); // Update UI with new like status
        },
        error: err => console.error('Error toggling like:', err),
      });
    }
  }
}
