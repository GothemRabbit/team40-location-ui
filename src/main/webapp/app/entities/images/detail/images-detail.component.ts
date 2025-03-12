import { Component, inject, input } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { DataUtils } from 'app/core/util/data-util.service';
import { IImages } from '../images.model';
import { ImagesService } from '../service/images.service';
import { ImagesDeleteDialogComponent } from '../delete/images-delete-dialog.component';

import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { filter, tap } from 'rxjs';

@Component({
  standalone: true,
  selector: 'jhi-images-detail',
  templateUrl: './images-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ImagesDetailComponent {
  images = input<IImages | null>(null);

  protected dataUtils = inject(DataUtils);
  protected readonly imagesService = inject(ImagesService);
  protected readonly activatedRoute = inject(ActivatedRoute);
  protected readonly router = inject(Router);
  protected modalService = inject(NgbModal);

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
  delete(images: IImages): void {
    // Open the delete dialog
    const modalRef = this.modalService.open(ImagesDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.images = images;
    // After the modal closes with 'ITEM_DELETED_EVENT', navigate away
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        tap(() => {
          this.router.navigate(['/images']); // or wherever you want to redirect
        }),
      )
      .subscribe();
  }
}
