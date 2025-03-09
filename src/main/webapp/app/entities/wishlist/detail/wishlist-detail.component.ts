import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IWishlist } from '../wishlist.model';

@Component({
  standalone: true,
  selector: 'jhi-wishlist-detail',
  templateUrl: './wishlist-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class WishlistDetailComponent {
  wishlist = input<IWishlist | null>(null);

  previousState(): void {
    window.history.back();
  }
}
