import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ILikes } from '../likes.model';
import { LikesService } from '../service/likes.service';

@Component({
  standalone: true,
  templateUrl: './likes-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class LikesDeleteDialogComponent {
  likes?: ILikes;

  protected likesService = inject(LikesService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.likesService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
