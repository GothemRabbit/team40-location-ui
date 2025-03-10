import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IProfileDetails } from 'app/entities/profile-details/profile-details.model';
import { ProfileDetailsService } from 'app/entities/profile-details/service/profile-details.service';
import { IItem } from 'app/entities/item/item.model';
import { ItemService } from 'app/entities/item/service/item.service';
import { IUserDetails } from 'app/entities/user-details/user-details.model';
import { UserDetailsService } from 'app/entities/user-details/service/user-details.service';
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

  profileDetailsSharedCollection: IProfileDetails[] = [];
  itemsSharedCollection: IItem[] = [];
  userDetailsSharedCollection: IUserDetails[] = [];

  protected wishlistService = inject(WishlistService);
  protected wishlistFormService = inject(WishlistFormService);
  protected profileDetailsService = inject(ProfileDetailsService);
  protected itemService = inject(ItemService);
  protected userDetailsService = inject(UserDetailsService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: WishlistFormGroup = this.wishlistFormService.createWishlistFormGroup();

  compareProfileDetails = (o1: IProfileDetails | null, o2: IProfileDetails | null): boolean =>
    this.profileDetailsService.compareProfileDetails(o1, o2);

  compareItem = (o1: IItem | null, o2: IItem | null): boolean => this.itemService.compareItem(o1, o2);

  compareUserDetails = (o1: IUserDetails | null, o2: IUserDetails | null): boolean => this.userDetailsService.compareUserDetails(o1, o2);

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

    this.profileDetailsSharedCollection = this.profileDetailsService.addProfileDetailsToCollectionIfMissing<IProfileDetails>(
      this.profileDetailsSharedCollection,
      wishlist.profileDetails,
    );
    this.itemsSharedCollection = this.itemService.addItemToCollectionIfMissing<IItem>(
      this.itemsSharedCollection,
      ...(wishlist.items ?? []),
    );
    this.userDetailsSharedCollection = this.userDetailsService.addUserDetailsToCollectionIfMissing<IUserDetails>(
      this.userDetailsSharedCollection,
      wishlist.userDetails,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.profileDetailsService
      .query()
      .pipe(map((res: HttpResponse<IProfileDetails[]>) => res.body ?? []))
      .pipe(
        map((profileDetails: IProfileDetails[]) =>
          this.profileDetailsService.addProfileDetailsToCollectionIfMissing<IProfileDetails>(profileDetails, this.wishlist?.profileDetails),
        ),
      )
      .subscribe((profileDetails: IProfileDetails[]) => (this.profileDetailsSharedCollection = profileDetails));

    this.itemService
      .query()
      .pipe(map((res: HttpResponse<IItem[]>) => res.body ?? []))
      .pipe(map((items: IItem[]) => this.itemService.addItemToCollectionIfMissing<IItem>(items, ...(this.wishlist?.items ?? []))))
      .subscribe((items: IItem[]) => (this.itemsSharedCollection = items));

    this.userDetailsService
      .query()
      .pipe(map((res: HttpResponse<IUserDetails[]>) => res.body ?? []))
      .pipe(
        map((userDetails: IUserDetails[]) =>
          this.userDetailsService.addUserDetailsToCollectionIfMissing<IUserDetails>(userDetails, this.wishlist?.userDetails),
        ),
      )
      .subscribe((userDetails: IUserDetails[]) => (this.userDetailsSharedCollection = userDetails));
  }
}
