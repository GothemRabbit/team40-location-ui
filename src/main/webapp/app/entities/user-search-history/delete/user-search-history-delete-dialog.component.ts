import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IUserSearchHistory } from '../user-search-history.model';
import { UserSearchHistoryService } from '../service/user-search-history.service';

@Component({
  standalone: true,
  templateUrl: './user-search-history-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class UserSearchHistoryDeleteDialogComponent {
  userSearchHistory?: IUserSearchHistory;

  protected userSearchHistoryService = inject(UserSearchHistoryService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.userSearchHistoryService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
