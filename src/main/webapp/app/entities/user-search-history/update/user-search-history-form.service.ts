import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IUserSearchHistory, NewUserSearchHistory } from '../user-search-history.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUserSearchHistory for edit and NewUserSearchHistoryFormGroupInput for create.
 */
type UserSearchHistoryFormGroupInput = IUserSearchHistory | PartialWithRequiredKeyOf<NewUserSearchHistory>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IUserSearchHistory | NewUserSearchHistory> = Omit<T, 'searchDate'> & {
  searchDate?: string | null;
};

type UserSearchHistoryFormRawValue = FormValueOf<IUserSearchHistory>;

type NewUserSearchHistoryFormRawValue = FormValueOf<NewUserSearchHistory>;

type UserSearchHistoryFormDefaults = Pick<NewUserSearchHistory, 'id' | 'searchDate'>;

type UserSearchHistoryFormGroupContent = {
  id: FormControl<UserSearchHistoryFormRawValue['id'] | NewUserSearchHistory['id']>;
  searchQuery: FormControl<UserSearchHistoryFormRawValue['searchQuery']>;
  filters: FormControl<UserSearchHistoryFormRawValue['filters']>;
  searchDate: FormControl<UserSearchHistoryFormRawValue['searchDate']>;
};

export type UserSearchHistoryFormGroup = FormGroup<UserSearchHistoryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UserSearchHistoryFormService {
  createUserSearchHistoryFormGroup(userSearchHistory: UserSearchHistoryFormGroupInput = { id: null }): UserSearchHistoryFormGroup {
    const userSearchHistoryRawValue = this.convertUserSearchHistoryToUserSearchHistoryRawValue({
      ...this.getFormDefaults(),
      ...userSearchHistory,
    });
    return new FormGroup<UserSearchHistoryFormGroupContent>({
      id: new FormControl(
        { value: userSearchHistoryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      searchQuery: new FormControl(userSearchHistoryRawValue.searchQuery, {
        validators: [Validators.required],
      }),
      filters: new FormControl(userSearchHistoryRawValue.filters),
      searchDate: new FormControl(userSearchHistoryRawValue.searchDate, {
        validators: [Validators.required],
      }),
    });
  }

  getUserSearchHistory(form: UserSearchHistoryFormGroup): IUserSearchHistory | NewUserSearchHistory {
    return this.convertUserSearchHistoryRawValueToUserSearchHistory(
      form.getRawValue() as UserSearchHistoryFormRawValue | NewUserSearchHistoryFormRawValue,
    );
  }

  resetForm(form: UserSearchHistoryFormGroup, userSearchHistory: UserSearchHistoryFormGroupInput): void {
    const userSearchHistoryRawValue = this.convertUserSearchHistoryToUserSearchHistoryRawValue({
      ...this.getFormDefaults(),
      ...userSearchHistory,
    });
    form.reset(
      {
        ...userSearchHistoryRawValue,
        id: { value: userSearchHistoryRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): UserSearchHistoryFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      searchDate: currentTime,
    };
  }

  private convertUserSearchHistoryRawValueToUserSearchHistory(
    rawUserSearchHistory: UserSearchHistoryFormRawValue | NewUserSearchHistoryFormRawValue,
  ): IUserSearchHistory | NewUserSearchHistory {
    return {
      ...rawUserSearchHistory,
      searchDate: dayjs(rawUserSearchHistory.searchDate, DATE_TIME_FORMAT),
    };
  }

  private convertUserSearchHistoryToUserSearchHistoryRawValue(
    userSearchHistory: IUserSearchHistory | (Partial<NewUserSearchHistory> & UserSearchHistoryFormDefaults),
  ): UserSearchHistoryFormRawValue | PartialWithRequiredKeyOf<NewUserSearchHistoryFormRawValue> {
    return {
      ...userSearchHistory,
      searchDate: userSearchHistory.searchDate ? userSearchHistory.searchDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
