import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IProfileDetails } from '../profile-details.model';
import { ProfileDetailsService } from '../service/profile-details.service';

@Component({
  standalone: true,
  templateUrl: './profile-details-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ProfileDetailsDeleteDialogComponent {
  profileDetails?: IProfileDetails;

  protected profileDetailsService = inject(ProfileDetailsService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.profileDetailsService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
