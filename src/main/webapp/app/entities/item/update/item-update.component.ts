import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IWishlist } from 'app/entities/wishlist/wishlist.model';
import { WishlistService } from 'app/entities/wishlist/service/wishlist.service';
import { IUserDetails } from 'app/entities/user-details/user-details.model';
import { UserDetailsService } from 'app/entities/user-details/service/user-details.service';
import { Condition } from 'app/entities/enumerations/condition.model';
import { Category } from 'app/entities/enumerations/category.model';
import { ItemService } from '../service/item.service';
import { IItem } from '../item.model';
import { ItemFormGroup, ItemFormService } from './item-form.service';

@Component({
  standalone: true,
  selector: 'jhi-item-update',
  templateUrl: './item-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ItemUpdateComponent implements OnInit {
  isSaving = false;
  item: IItem | null = null;
  conditionValues = Object.keys(Condition);
  categoryValues = Object.keys(Category);

  wishlistsSharedCollection: IWishlist[] = [];
  userDetailsSharedCollection: IUserDetails[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected itemService = inject(ItemService);
  protected itemFormService = inject(ItemFormService);
  protected wishlistService = inject(WishlistService);
  protected userDetailsService = inject(UserDetailsService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ItemFormGroup = this.itemFormService.createItemFormGroup();

  compareWishlist = (o1: IWishlist | null, o2: IWishlist | null): boolean => this.wishlistService.compareWishlist(o1, o2);

  compareUserDetails = (o1: IUserDetails | null, o2: IUserDetails | null): boolean => this.userDetailsService.compareUserDetails(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ item }) => {
      this.item = item;
      if (item) {
        this.updateForm(item);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('teamproject24App.error', { message: err.message })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const item = this.itemFormService.getItem(this.editForm);
    if (item.id !== null) {
      this.subscribeToSaveResponse(this.itemService.update(item));
    } else {
      this.subscribeToSaveResponse(this.itemService.create(item));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IItem>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(item: IItem): void {
    this.item = item;
    this.itemFormService.resetForm(this.editForm, item);

    this.wishlistsSharedCollection = this.wishlistService.addWishlistToCollectionIfMissing<IWishlist>(
      this.wishlistsSharedCollection,
      ...(item.wishlists ?? []),
    );
    this.userDetailsSharedCollection = this.userDetailsService.addUserDetailsToCollectionIfMissing<IUserDetails>(
      this.userDetailsSharedCollection,
      item.seller,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.wishlistService
      .query()
      .pipe(map((res: HttpResponse<IWishlist[]>) => res.body ?? []))
      .pipe(
        map((wishlists: IWishlist[]) =>
          this.wishlistService.addWishlistToCollectionIfMissing<IWishlist>(wishlists, ...(this.item?.wishlists ?? [])),
        ),
      )
      .subscribe((wishlists: IWishlist[]) => (this.wishlistsSharedCollection = wishlists));

    this.userDetailsService
      .query()
      .pipe(map((res: HttpResponse<IUserDetails[]>) => res.body ?? []))
      .pipe(
        map((userDetails: IUserDetails[]) =>
          this.userDetailsService.addUserDetailsToCollectionIfMissing<IUserDetails>(userDetails, this.item?.seller),
        ),
      )
      .subscribe((userDetails: IUserDetails[]) => (this.userDetailsSharedCollection = userDetails));
  }
}
