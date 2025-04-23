import { Component, NgZone, OnInit, inject } from '@angular/core';
import { ActivatedRoute, Data, ParamMap, Router, RouterModule } from '@angular/router';
import { Observable, Subscription, combineLatest, filter, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { SortByDirective, SortDirective, SortService, type SortState, sortStateSignal } from 'app/shared/sort';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { DEFAULT_SORT_DATA, ITEM_DELETED_EVENT, SORT } from 'app/config/navigation.constants';
import { DataUtils } from 'app/core/util/data-util.service';
import { IReview } from '../review.model';
import { EntityArrayResponseType, ReviewService } from '../service/review.service';
import { ReviewDeleteDialogComponent } from '../delete/review-delete-dialog.component';
import { faFilter, faArrowUp, faArrowDown } from '@fortawesome/free-solid-svg-icons';

@Component({
  standalone: true,
  selector: 'jhi-review',
  templateUrl: './review.component.html',
  styleUrl: './review.component.scss',
  imports: [
    RouterModule,
    FormsModule,
    SharedModule,
    SortDirective,
    SortByDirective,
    DurationPipe,
    FormatMediumDatetimePipe,
    FormatMediumDatePipe,
    ReactiveFormsModule,
  ],
})
export class ReviewComponent implements OnInit {
  subscription: Subscription | null = null;
  reviews?: IReview[];
  isLoading = false;
  sort?: boolean = false;

  sortState = sortStateSignal({});

  public readonly router = inject(Router);
  protected readonly reviewService = inject(ReviewService);
  protected readonly activatedRoute = inject(ActivatedRoute);
  protected readonly sortService = inject(SortService);
  protected dataUtils = inject(DataUtils);
  protected modalService = inject(NgbModal);
  protected ngZone = inject(NgZone);
  protected readonly faFilter = faFilter;
  protected readonly faArrowUp = faArrowUp;
  protected readonly faArrowDown = faArrowDown;

  trackId = (item: IReview): number => this.reviewService.getReviewIdentifier(item);
  ngOnInit(): void {
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => {
          if (!this.reviews || this.reviews.length === 0) {
            this.load();
          }
        }),
      )
      .subscribe();
  }
  getAverageRating(): number {
    if (this.reviews == null || this.reviews.length === 0) {
      return 0;
    }
    const average: number = this.reviews.reduce((total, r) => Number(r.rating) + total, 0);
    return Number((average / this.reviews.length).toFixed(2));
  }
  filterBy(Input: HTMLSelectElement): void {
    if (Number(Input.value)) {
      this.reviews = this.reviews?.filter(r => Number(r.rating) === Number(Input.value));
    } else {
      this.load();
    }
  }

  filterByRetailer(Input: HTMLInputElement): void {
    if (Input.value) {
      this.reviews = this.reviews?.filter(r => r.retailer?.userName === Input.value);
    }
  }
  public sortReviewASC(): void {
    this.reviews = this.reviews?.sort((a, b) => new Date(b.date?.date() ?? 0).getTime() - new Date(a.date?.date() ?? 0).getTime());
  }
  public sortReviewDSC(): void {
    this.reviews = this.reviews?.sort((a, b) => new Date(a.date?.date() ?? 0).getTime() - new Date(b.date?.date() ?? 0).getTime());
  }
  public sortButton(): void {
    if (!this.sort) {
      this.sortReviewDSC();
    } else {
      this.sortReviewASC();
    }
    this.sort = !this.sort;
  }
  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  likeButton(review: IReview): void {
    review.liked = !review.liked;
  }

  delete(review: IReview): void {
    const modalRef = this.modalService.open(ReviewDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.review = review;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        tap(() => this.load()),
      )
      .subscribe();
  }

  load(): void {
    this.queryBackend().subscribe({
      next: (res: EntityArrayResponseType) => {
        this.onResponseSuccess(res);
      },
    });
  }

  navigateToWithComponentValues(event: SortState): void {
    this.reviews = [];
    this.handleNavigation(event);
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    this.sortState.set(this.sortService.parseSortParam(params.get(SORT) ?? data[DEFAULT_SORT_DATA]));
  }

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.reviews = this.refineData(dataFromBody);
  }

  protected refineData(data: IReview[]): IReview[] {
    const { predicate, order } = this.sortState();
    return predicate && order ? data.sort(this.sortService.startSort({ predicate, order })) : data;
  }

  protected fillComponentAttributesFromResponseBody(data: IReview[] | null): IReview[] {
    return data ?? [];
  }

  protected queryBackend(): Observable<EntityArrayResponseType> {
    this.isLoading = true;
    const queryObject: any = {
      sort: this.sortService.buildSortParam(this.sortState()),
    };
    return this.reviewService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
  }

  protected handleNavigation(sortState: SortState): void {
    const queryParamsObj = {
      sort: this.sortService.buildSortParam(sortState),
    };

    this.ngZone.run(() => {
      this.router.navigate(['./'], {
        relativeTo: this.activatedRoute,
        queryParams: queryParamsObj,
      });
    });
  }
}
