import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IUserInteraction, NewUserInteraction } from '../user-interaction.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUserInteraction for edit and NewUserInteractionFormGroupInput for create.
 */
type UserInteractionFormGroupInput = IUserInteraction | PartialWithRequiredKeyOf<NewUserInteraction>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IUserInteraction | NewUserInteraction> = Omit<T, 'interactionDate'> & {
  interactionDate?: string | null;
};

type UserInteractionFormRawValue = FormValueOf<IUserInteraction>;

type NewUserInteractionFormRawValue = FormValueOf<NewUserInteraction>;

type UserInteractionFormDefaults = Pick<NewUserInteraction, 'id' | 'interactionDate'>;

type UserInteractionFormGroupContent = {
  id: FormControl<UserInteractionFormRawValue['id'] | NewUserInteraction['id']>;
  type: FormControl<UserInteractionFormRawValue['type']>;
  details: FormControl<UserInteractionFormRawValue['details']>;
  interactionDate: FormControl<UserInteractionFormRawValue['interactionDate']>;
};

export type UserInteractionFormGroup = FormGroup<UserInteractionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UserInteractionFormService {
  createUserInteractionFormGroup(userInteraction: UserInteractionFormGroupInput = { id: null }): UserInteractionFormGroup {
    const userInteractionRawValue = this.convertUserInteractionToUserInteractionRawValue({
      ...this.getFormDefaults(),
      ...userInteraction,
    });
    return new FormGroup<UserInteractionFormGroupContent>({
      id: new FormControl(
        { value: userInteractionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      type: new FormControl(userInteractionRawValue.type, {
        validators: [Validators.required],
      }),
      details: new FormControl(userInteractionRawValue.details),
      interactionDate: new FormControl(userInteractionRawValue.interactionDate, {
        validators: [Validators.required],
      }),
    });
  }

  getUserInteraction(form: UserInteractionFormGroup): IUserInteraction | NewUserInteraction {
    return this.convertUserInteractionRawValueToUserInteraction(
      form.getRawValue() as UserInteractionFormRawValue | NewUserInteractionFormRawValue,
    );
  }

  resetForm(form: UserInteractionFormGroup, userInteraction: UserInteractionFormGroupInput): void {
    const userInteractionRawValue = this.convertUserInteractionToUserInteractionRawValue({ ...this.getFormDefaults(), ...userInteraction });
    form.reset(
      {
        ...userInteractionRawValue,
        id: { value: userInteractionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): UserInteractionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      interactionDate: currentTime,
    };
  }

  private convertUserInteractionRawValueToUserInteraction(
    rawUserInteraction: UserInteractionFormRawValue | NewUserInteractionFormRawValue,
  ): IUserInteraction | NewUserInteraction {
    return {
      ...rawUserInteraction,
      interactionDate: dayjs(rawUserInteraction.interactionDate, DATE_TIME_FORMAT),
    };
  }

  private convertUserInteractionToUserInteractionRawValue(
    userInteraction: IUserInteraction | (Partial<NewUserInteraction> & UserInteractionFormDefaults),
  ): UserInteractionFormRawValue | PartialWithRequiredKeyOf<NewUserInteractionFormRawValue> {
    return {
      ...userInteraction,
      interactionDate: userInteraction.interactionDate ? userInteraction.interactionDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
