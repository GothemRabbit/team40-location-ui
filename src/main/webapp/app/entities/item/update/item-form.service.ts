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

type ItemFormDefaults = Pick<NewItem, 'id' | 'timeListed' | 'itemLike'>;

type ItemFormGroupContent = {
  id: FormControl<ItemFormRawValue['id'] | NewItem['id']>;
  itemTitle: FormControl<ItemFormRawValue['itemTitle']>;
  itemPrice: FormControl<ItemFormRawValue['itemPrice']>;
  itemSize: FormControl<ItemFormRawValue['itemSize']>;
  itemCondition: FormControl<ItemFormRawValue['itemCondition']>;
  itemCategory: FormControl<ItemFormRawValue['itemCategory']>;
  description: FormControl<ItemFormRawValue['description']>;
  itemColour: FormControl<ItemFormRawValue['itemColour']>;
  itemImage: FormControl<ItemFormRawValue['itemImage']>;
  itemImageContentType: FormControl<ItemFormRawValue['itemImageContentType']>;
  timeListed: FormControl<ItemFormRawValue['timeListed']>;
  itemLike: FormControl<ItemFormRawValue['itemLike']>;
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
      itemTitle: new FormControl(itemRawValue.itemTitle, {
        validators: [Validators.required],
      }),
      itemPrice: new FormControl(itemRawValue.itemPrice, {
        validators: [Validators.required],
      }),
      itemSize: new FormControl(itemRawValue.itemSize),
      itemCondition: new FormControl(itemRawValue.itemCondition, {
        validators: [Validators.required],
      }),
      itemCategory: new FormControl(itemRawValue.itemCategory, {
        validators: [Validators.required],
      }),
      description: new FormControl(itemRawValue.description),
      itemColour: new FormControl(itemRawValue.itemColour),
      itemImage: new FormControl(itemRawValue.itemImage, {
        validators: [Validators.required],
      }),
      itemImageContentType: new FormControl(itemRawValue.itemImageContentType),
      timeListed: new FormControl(itemRawValue.timeListed, {
        validators: [Validators.required],
      }),
      itemLike: new FormControl(itemRawValue.itemLike),
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
      itemLike: false,
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
    };
  }
}
