import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUserDetails } from 'app/entities/user-details/user-details.model';
import { UserDetailsService } from 'app/entities/user-details/service/user-details.service';
import { IItem } from 'app/entities/item/item.model';
import { ItemService } from 'app/entities/item/service/item.service';
import { VisibilityType } from 'app/entities/enumerations/visibility-type.model';
import { WishlistService } from '../service/wishlist.service';
import { IWishlist } from '../wishlist.model';
import { WishlistFormGroup, WishlistFormService } from './wishlist-form.service';

@Component({
  standalone: true,
  selector: 'jhi-wishlist-update',
  templateUrl: './wishlist-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class WishlistUpdateComponent implements OnInit {
  isSaving = false;
  wishlist: IWishlist | null = null;
  visibilityTypeValues = Object.keys(VisibilityType);

  userDetailsSharedCollection: IUserDetails[] = [];
  itemsSharedCollection: IItem[] = [];

  protected wishlistService = inject(WishlistService);
  protected wishlistFormService = inject(WishlistFormService);
  protected userDetailsService = inject(UserDetailsService);
  protected itemService = inject(ItemService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: WishlistFormGroup = this.wishlistFormService.createWishlistFormGroup();

  compareUserDetails = (o1: IUserDetails | null, o2: IUserDetails | null): boolean => this.userDetailsService.compareUserDetails(o1, o2);

  compareItem = (o1: IItem | null, o2: IItem | null): boolean => this.itemService.compareItem(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ wishlist }) => {
      this.wishlist = wishlist;
      if (wishlist) {
        this.updateForm(wishlist);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const wishlist = this.wishlistFormService.getWishlist(this.editForm);
    if (wishlist.id !== null) {
      this.subscribeToSaveResponse(this.wishlistService.update(wishlist));
    } else {
      this.subscribeToSaveResponse(this.wishlistService.create(wishlist));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWishlist>>): void {
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

  protected updateForm(wishlist: IWishlist): void {
    this.wishlist = wishlist;
    this.wishlistFormService.resetForm(this.editForm, wishlist);

    this.userDetailsSharedCollection = this.userDetailsService.addUserDetailsToCollectionIfMissing<IUserDetails>(
      this.userDetailsSharedCollection,
      wishlist.userDetails,
    );
    this.itemsSharedCollection = this.itemService.addItemToCollectionIfMissing<IItem>(
      this.itemsSharedCollection,
      ...(wishlist.items ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userDetailsService
      .query()
      .pipe(map((res: HttpResponse<IUserDetails[]>) => res.body ?? []))
      .pipe(
        map((userDetails: IUserDetails[]) =>
          this.userDetailsService.addUserDetailsToCollectionIfMissing<IUserDetails>(userDetails, this.wishlist?.userDetails),
        ),
      )
      .subscribe((userDetails: IUserDetails[]) => (this.userDetailsSharedCollection = userDetails));

    this.itemService
      .query()
      .pipe(map((res: HttpResponse<IItem[]>) => res.body ?? []))
      .pipe(map((items: IItem[]) => this.itemService.addItemToCollectionIfMissing<IItem>(items, ...(this.wishlist?.items ?? []))))
      .subscribe((items: IItem[]) => (this.itemsSharedCollection = items));
  }
}
