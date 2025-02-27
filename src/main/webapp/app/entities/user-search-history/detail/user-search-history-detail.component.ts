import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IUserSearchHistory } from '../user-search-history.model';

@Component({
  standalone: true,
  selector: 'jhi-user-search-history-detail',
  templateUrl: './user-search-history-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class UserSearchHistoryDetailComponent {
  userSearchHistory = input<IUserSearchHistory | null>(null);

  previousState(): void {
    window.history.back();
  }
}
