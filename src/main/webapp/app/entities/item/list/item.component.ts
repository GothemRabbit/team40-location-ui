import { Component, NgZone, OnInit, WritableSignal, computed, inject, signal } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Router, RouterModule } from '@angular/router';
import { Observable, Subscription, combineLatest, filter, tap, forkJoin } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { SortByDirective, SortDirective, SortService, type SortState, sortStateSignal } from 'app/shared/sort';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { FormsModule } from '@angular/forms';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { DEFAULT_SORT_DATA, ITEM_DELETED_EVENT, SORT } from 'app/config/navigation.constants';
import { DataUtils } from 'app/core/util/data-util.service';
import { ParseLinks } from 'app/core/util/parse-links.service';
import { InfiniteScrollDirective } from 'ngx-infinite-scroll';
import { ItemDeleteDialogComponent } from '../delete/item-delete-dialog.component';
import { EntityArrayResponseType, ItemService } from '../service/item.service';
import { IItem } from '../item.model';
import { ILikes } from '../../likes/likes.model';
import { LikesService } from '../../likes/service/likes.service';
import { ProfileDetailsService } from '../../profile-details/service/profile-details.service';
import { AccountService } from '../../../core/auth/account.service';
import { IImages } from '../../images/images.model';

@Component({
  standalone: true,
  selector: 'jhi-item',
  templateUrl: './item.component.html',
  imports: [
    RouterModule,
    FormsModule,
    SharedModule,
    SortDirective,
    SortByDirective,
    DurationPipe,
    FormatMediumDatetimePipe,
    FormatMediumDatePipe,
    InfiniteScrollDirective,
  ],
})
export class ItemComponent implements OnInit {
  username?: string;
  subscription: Subscription | null = null;
  items?: IItem[];
  isLoading = false;

  sortState = sortStateSignal({});

  itemsPerPage = ITEMS_PER_PAGE;
  links: WritableSignal<Record<string, undefined | Record<string, string | undefined>>> = signal({});
  hasMorePage = computed(() => !!this.links().next);
  isFirstFetch = computed(() => Object.keys(this.links()).length === 0);

  public readonly router = inject(Router);
  protected readonly itemService = inject(ItemService);
  protected readonly activatedRoute = inject(ActivatedRoute);
  protected readonly sortService = inject(SortService);
  protected parseLinks = inject(ParseLinks);
  protected dataUtils = inject(DataUtils);
  protected modalService = inject(NgbModal);
  protected ngZone = inject(NgZone);
  private readonly accountService = inject(AccountService);
  private readonly profileDetailsService = inject(ProfileDetailsService);
  //private likeService : LikesService;

  trackId = (item: IItem): number => this.itemService.getItemIdentifier(item);

  ngOnInit(): void {
    this.username = this.accountService.getCurrentUserusername();

    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => this.reset()),
        tap(() => this.load()),
      )
      .subscribe();
  }

  loadAll(): void {
    this.itemService.query().subscribe(response => {
      this.items = response.body ?? [];
      this.loadImagesForItems();
    });
  }

  loadImagesForItems(): void {
    if (!this.items || this.items.length === 0) {
      console.error('No items found to load images for.');
      return;
    }

    const requests: Observable<IImages[]>[] = this.items.map(item => this.itemService.getImagesForItem(item.id));

    forkJoin(requests).subscribe((imagesArray: IImages[][]) => {
      if (!this.items) {
        console.error('this.items is undefined inside subscribe');
        return;
      }

      this.items.forEach((item: IItem, index: number) => {
        item.images = imagesArray[index] ?? [];
      });
    });
  }

  reset(): void {
    this.items = [];
  }

  loadNextPage(): void {
    this.load();
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  // isLiked(item: IItem): boolean {
  //   // Check if the current profile has liked the item
  //   return item.likes?.some(like => like.profileDetails?.id == this.username) ?? false;
  // }

  toggleLike(item: IItem): void {
    if (!this.username) {
      console.error('Profile ID not loaded.');
      return;
    }
  }

  // // ⭐ Toggle Dropdown Visibility
  // toggleDropdown(item: IItem): void {
  //   item.dropDown = !item.dropDown;
  // }

  delete(item: IItem): void {
    const modalRef = this.modalService.open(ItemDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.item = item;
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
    this.handleNavigation(event);
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    this.sortState.set(this.sortService.parseSortParam(params.get(SORT) ?? data[DEFAULT_SORT_DATA]));
  }

  // protected onResponseSuccess(response: EntityArrayResponseType): void {
  //   this.fillComponentAttributesFromResponseHeader(response.headers);
  //   const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
  //   this.items = dataFromBody;
  // }

  protected fillComponentAttributesFromResponseBody(data: IItem[] | null): IItem[] {
    // If there is previous link, data is a infinite scroll pagination content.
    if (this.links().prev) {
      const itemsNew = this.items ?? [];
      if (data) {
        for (const d of data) {
          if (itemsNew.some(op => op.id === d.id)) {
            itemsNew.push(d);
          }
        }
      }
      return itemsNew;
    }
    return data ?? [];
  }

  protected fillComponentAttributesFromResponseHeader(headers: HttpHeaders): void {
    const linkHeader = headers.get('link');
    if (linkHeader) {
      this.links.set(this.parseLinks.parseAll(linkHeader));
    } else {
      this.links.set({});
    }
  }

  protected queryBackend(): Observable<EntityArrayResponseType> {
    this.isLoading = true;
    const queryObject: any = {
      size: this.itemsPerPage,
      eagerload: true,
    };
    if (this.hasMorePage()) {
      Object.assign(queryObject, this.links().next);
    } else if (this.isFirstFetch()) {
      Object.assign(queryObject, { sort: this.sortService.buildSortParam(this.sortState()) });
    }

    return this.itemService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
  }

  protected handleNavigation(sortState: SortState): void {
    this.links.set({});

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

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    this.fillComponentAttributesFromResponseHeader(response.headers);
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);

    this.items = dataFromBody.map(item => ({
      ...item,
      images: item.images ?? [],
    }));
  }
}
