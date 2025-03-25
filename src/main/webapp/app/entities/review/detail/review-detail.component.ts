import { Component, inject, input, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { DataUtils } from 'app/core/util/data-util.service';
import { IReview } from '../review.model';
import { ReviewDeleteDialogComponent } from '../delete/review-delete-dialog.component';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { filter, tap } from 'rxjs';
import { Account } from '../../../core/auth/account.model';
import { AccountService } from '../../../core/auth/account.service';

@Component({
  standalone: true,
  selector: 'jhi-review-detail',
  templateUrl: './review-detail.component.html',
  styleUrl: './review-detail.component.scss',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ReviewDetailComponent implements OnInit {
  review = input<IReview | null>(null);
  isOwner = false;
  account: Account | null = null;

  public readonly router = inject(Router);
  protected dataUtils = inject(DataUtils);
  protected modalService = inject(NgbModal);
  protected accountService = inject(AccountService);

  ngOnInit(): void {
    this.accountService.identity().subscribe(account => {
      this.account = account;
      this.checkOwnership();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }

  delete(review: IReview): void {
    const modalRef = this.modalService.open(ReviewDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.review = review;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        tap(() => {
          this.router.navigate(['./review']);
        }),
      )
      .subscribe();
  }

  checkOwnership(): void {
    const user = this.review();
    // eslint-disable-next-line no-console
    console.log(user?.consumer);
    // eslint-disable-next-line no-console
    console.log(this.account?.login);
    this.isOwner = this.account?.login === this.review()?.consumer?.userName;
    // eslint-disable-next-line no-console
    console.log(this.isOwner);
  }
}
