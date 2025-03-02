import { Component, inject, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { DataUtils } from 'app/core/util/data-util.service';
import { IUserDetails } from '../user-details.model';
import { ItemComponent } from '../../item/list/item.component';

@Component({
  standalone: true,
  selector: 'jhi-user-details-detail',
  templateUrl: './user-details-detail.component.html',
  styleUrl: './user-details-detail.component.scss',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe, ItemComponent],
})
export class UserDetailsDetailComponent {
  userDetails = input<IUserDetails | null>(null);
  activeTab = 'listings';

  protected dataUtils = inject(DataUtils);

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
