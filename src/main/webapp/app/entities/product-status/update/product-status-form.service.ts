import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IProductStatus, NewProductStatus } from '../product-status.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProductStatus for edit and NewProductStatusFormGroupInput for create.
 */
type ProductStatusFormGroupInput = IProductStatus | PartialWithRequiredKeyOf<NewProductStatus>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IProductStatus | NewProductStatus> = Omit<T, 'meetingTime' | 'updatedAt' | 'createdAt'> & {
  meetingTime?: string | null;
  updatedAt?: string | null;
  createdAt?: string | null;
};

type ProductStatusFormRawValue = FormValueOf<IProductStatus>;

type NewProductStatusFormRawValue = FormValueOf<NewProductStatus>;

type ProductStatusFormDefaults = Pick<NewProductStatus, 'id' | 'meetingTime' | 'updatedAt' | 'createdAt'>;

type ProductStatusFormGroupContent = {
  id: FormControl<ProductStatusFormRawValue['id'] | NewProductStatus['id']>;
  status: FormControl<ProductStatusFormRawValue['status']>;
  meetingTime: FormControl<ProductStatusFormRawValue['meetingTime']>;
  updatedAt: FormControl<ProductStatusFormRawValue['updatedAt']>;
  createdAt: FormControl<ProductStatusFormRawValue['createdAt']>;
  item: FormControl<ProductStatusFormRawValue['item']>;
  conversation: FormControl<ProductStatusFormRawValue['conversation']>;
  profileDetails: FormControl<ProductStatusFormRawValue['profileDetails']>;
  profileDetails1: FormControl<ProductStatusFormRawValue['profileDetails']>;
  location: FormControl<ProductStatusFormRawValue['location']>;
};

export type ProductStatusFormGroup = FormGroup<ProductStatusFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProductStatusFormService {
  createProductStatusFormGroup(productStatus: ProductStatusFormGroupInput = { id: null }): ProductStatusFormGroup {
    const productStatusRawValue = this.convertProductStatusToProductStatusRawValue({
      ...this.getFormDefaults(),
      ...productStatus,
    });
    return new FormGroup<ProductStatusFormGroupContent>({
      id: new FormControl(
        { value: productStatusRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      status: new FormControl(productStatusRawValue.status, {
        validators: [Validators.required],
      }),
      meetingTime: new FormControl(productStatusRawValue.meetingTime),
      updatedAt: new FormControl(productStatusRawValue.updatedAt),
      createdAt: new FormControl(productStatusRawValue.createdAt),
      item: new FormControl(productStatusRawValue.item),
      conversation: new FormControl(productStatusRawValue.conversation),
      profileDetails: new FormControl(productStatusRawValue.profileDetails),
      profileDetails1: new FormControl(productStatusRawValue.profileDetails1),
      location: new FormControl(productStatusRawValue.location),
    });
  }

  getProductStatus(form: ProductStatusFormGroup): IProductStatus | NewProductStatus {
    return this.convertProductStatusRawValueToProductStatus(form.getRawValue() as ProductStatusFormRawValue | NewProductStatusFormRawValue);
  }

  resetForm(form: ProductStatusFormGroup, productStatus: ProductStatusFormGroupInput): void {
    const productStatusRawValue = this.convertProductStatusToProductStatusRawValue({ ...this.getFormDefaults(), ...productStatus });
    form.reset(
      {
        ...productStatusRawValue,
        id: { value: productStatusRawValue.id, disabled: true },
        // ★ 同时重置 profileDetails1
        profileDetails1: productStatusRawValue.profileDetails1,
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ProductStatusFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      meetingTime: currentTime,
      updatedAt: currentTime,
      createdAt: currentTime,
    };
  }

  private convertProductStatusRawValueToProductStatus(
    rawProductStatus: ProductStatusFormRawValue | NewProductStatusFormRawValue,
  ): IProductStatus | NewProductStatus {
    return {
      ...rawProductStatus,
      meetingTime: dayjs(rawProductStatus.meetingTime, DATE_TIME_FORMAT),
      updatedAt: dayjs(rawProductStatus.updatedAt, DATE_TIME_FORMAT),
      createdAt: dayjs(rawProductStatus.createdAt, DATE_TIME_FORMAT),
    };
  }

  private convertProductStatusToProductStatusRawValue(
    productStatus: IProductStatus | (Partial<NewProductStatus> & ProductStatusFormDefaults),
  ): ProductStatusFormRawValue | PartialWithRequiredKeyOf<NewProductStatusFormRawValue> {
    return {
      ...productStatus,
      meetingTime: productStatus.meetingTime ? productStatus.meetingTime.format(DATE_TIME_FORMAT) : undefined,
      updatedAt: productStatus.updatedAt ? productStatus.updatedAt.format(DATE_TIME_FORMAT) : undefined,
      createdAt: productStatus.createdAt ? productStatus.createdAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
