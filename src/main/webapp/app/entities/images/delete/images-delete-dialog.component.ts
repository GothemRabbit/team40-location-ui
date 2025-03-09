import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IImages } from '../images.model';
import { ImagesService } from '../service/images.service';

@Component({
  standalone: true,
  templateUrl: './images-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ImagesDeleteDialogComponent {
  images?: IImages;

  protected imagesService = inject(ImagesService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.imagesService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
