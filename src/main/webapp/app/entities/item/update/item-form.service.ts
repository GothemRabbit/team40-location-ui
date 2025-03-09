import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IItem, NewItem } from '../item.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IItem for edit and NewItemFormGroupInput for create.
 */
type ItemFormGroupInput = IItem | PartialWithRequiredKeyOf<NewItem>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IItem | NewItem> = Omit<T, 'timeListed'> & {
  timeListed?: string | null;
};

type ItemFormRawValue = FormValueOf<IItem>;

type NewItemFormRawValue = FormValueOf<NewItem>;

type ItemFormDefaults = Pick<NewItem, 'id' | 'timeListed' | 'wishlists'>;

type ItemFormGroupContent = {
  id: FormControl<ItemFormRawValue['id'] | NewItem['id']>;
  title: FormControl<ItemFormRawValue['title']>;
  price: FormControl<ItemFormRawValue['price']>;
  condition: FormControl<ItemFormRawValue['condition']>;
  category: FormControl<ItemFormRawValue['category']>;
  description: FormControl<ItemFormRawValue['description']>;
  sizeItem: FormControl<ItemFormRawValue['sizeItem']>;
  brand: FormControl<ItemFormRawValue['brand']>;
  colour: FormControl<ItemFormRawValue['colour']>;
  timeListed: FormControl<ItemFormRawValue['timeListed']>;
  wishlists: FormControl<ItemFormRawValue['wishlists']>;
  profileDetails: FormControl<ItemFormRawValue['profileDetails']>;
  seller: FormControl<ItemFormRawValue['seller']>;
};

export type ItemFormGroup = FormGroup<ItemFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ItemFormService {
  createItemFormGroup(item: ItemFormGroupInput = { id: null }): ItemFormGroup {
    const itemRawValue = this.convertItemToItemRawValue({
      ...this.getFormDefaults(),
      ...item,
    });
    return new FormGroup<ItemFormGroupContent>({
      id: new FormControl(
        { value: itemRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      title: new FormControl(itemRawValue.title, {
        validators: [Validators.required, Validators.minLength(3), Validators.maxLength(100)],
      }),
      price: new FormControl(itemRawValue.price, {
        validators: [Validators.required, Validators.min(0)],
      }),
      condition: new FormControl(itemRawValue.condition, {
        validators: [Validators.required],
      }),
      category: new FormControl(itemRawValue.category, {
        validators: [Validators.required],
      }),
      description: new FormControl(itemRawValue.description),
      sizeItem: new FormControl(itemRawValue.sizeItem),
      brand: new FormControl(itemRawValue.brand),
      colour: new FormControl(itemRawValue.colour),
      timeListed: new FormControl(itemRawValue.timeListed, {
        validators: [Validators.required],
      }),
      wishlists: new FormControl(itemRawValue.wishlists ?? []),
      profileDetails: new FormControl(itemRawValue.profileDetails),
      seller: new FormControl(itemRawValue.seller),
    });
  }

  getItem(form: ItemFormGroup): IItem | NewItem {
    return this.convertItemRawValueToItem(form.getRawValue() as ItemFormRawValue | NewItemFormRawValue);
  }

  resetForm(form: ItemFormGroup, item: ItemFormGroupInput): void {
    const itemRawValue = this.convertItemToItemRawValue({ ...this.getFormDefaults(), ...item });
    form.reset(
      {
        ...itemRawValue,
        id: { value: itemRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ItemFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      timeListed: currentTime,
      wishlists: [],
    };
  }

  private convertItemRawValueToItem(rawItem: ItemFormRawValue | NewItemFormRawValue): IItem | NewItem {
    return {
      ...rawItem,
      timeListed: dayjs(rawItem.timeListed, DATE_TIME_FORMAT),
    };
  }

  private convertItemToItemRawValue(
    item: IItem | (Partial<NewItem> & ItemFormDefaults),
  ): ItemFormRawValue | PartialWithRequiredKeyOf<NewItemFormRawValue> {
    return {
      ...item,
      timeListed: item.timeListed ? item.timeListed.format(DATE_TIME_FORMAT) : undefined,
      wishlists: item.wishlists ?? [],
    };
  }
}
