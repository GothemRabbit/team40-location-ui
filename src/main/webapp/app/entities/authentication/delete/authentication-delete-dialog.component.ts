import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAuthentication } from '../authentication.model';
import { AuthenticationService } from '../service/authentication.service';

@Component({
  standalone: true,
  templateUrl: './authentication-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AuthenticationDeleteDialogComponent {
  authentication?: IAuthentication;

  protected authenticationService = inject(AuthenticationService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.authenticationService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
