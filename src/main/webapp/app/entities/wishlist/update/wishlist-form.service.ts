import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IWishlist, NewWishlist } from '../wishlist.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IWishlist for edit and NewWishlistFormGroupInput for create.
 */
type WishlistFormGroupInput = IWishlist | PartialWithRequiredKeyOf<NewWishlist>;

type WishlistFormDefaults = Pick<NewWishlist, 'id' | 'items'>;

type WishlistFormGroupContent = {
  id: FormControl<IWishlist['id'] | NewWishlist['id']>;
  name: FormControl<IWishlist['name']>;
  visibility: FormControl<IWishlist['visibility']>;
  userDetails: FormControl<IWishlist['userDetails']>;
  items: FormControl<IWishlist['items']>;
};

export type WishlistFormGroup = FormGroup<WishlistFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class WishlistFormService {
  createWishlistFormGroup(wishlist: WishlistFormGroupInput = { id: null }): WishlistFormGroup {
    const wishlistRawValue = {
      ...this.getFormDefaults(),
      ...wishlist,
    };
    return new FormGroup<WishlistFormGroupContent>({
      id: new FormControl(
        { value: wishlistRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(wishlistRawValue.name, {
        validators: [Validators.required],
      }),
      visibility: new FormControl(wishlistRawValue.visibility, {
        validators: [Validators.required],
      }),
      userDetails: new FormControl(wishlistRawValue.userDetails),
      items: new FormControl(wishlistRawValue.items ?? []),
    });
  }

  getWishlist(form: WishlistFormGroup): IWishlist | NewWishlist {
    return form.getRawValue() as IWishlist | NewWishlist;
  }

  resetForm(form: WishlistFormGroup, wishlist: WishlistFormGroupInput): void {
    const wishlistRawValue = { ...this.getFormDefaults(), ...wishlist };
    form.reset(
      {
        ...wishlistRawValue,
        id: { value: wishlistRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): WishlistFormDefaults {
    return {
      id: null,
      items: [],
    };
  }
}
