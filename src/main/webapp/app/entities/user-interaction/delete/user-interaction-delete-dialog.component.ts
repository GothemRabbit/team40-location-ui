import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IUserInteraction } from '../user-interaction.model';
import { UserInteractionService } from '../service/user-interaction.service';

@Component({
  standalone: true,
  templateUrl: './user-interaction-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class UserInteractionDeleteDialogComponent {
  userInteraction?: IUserInteraction;

  protected userInteractionService = inject(UserInteractionService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.userInteractionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
