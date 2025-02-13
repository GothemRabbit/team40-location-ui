import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IProductStatus } from '../product-status.model';

@Component({
  standalone: true,
  selector: 'jhi-product-status-detail',
  templateUrl: './product-status-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ProductStatusDetailComponent {
  productStatus = input<IProductStatus | null>(null);

  previousState(): void {
    window.history.back();
  }
}
