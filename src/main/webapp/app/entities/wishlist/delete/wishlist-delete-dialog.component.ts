import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IWishlist } from '../wishlist.model';
import { WishlistService } from '../service/wishlist.service';

@Component({
  standalone: true,
  templateUrl: './wishlist-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class WishlistDeleteDialogComponent {
  wishlist?: IWishlist;

  protected wishlistService = inject(WishlistService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.wishlistService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
