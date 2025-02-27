import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IUserRecommendation, NewUserRecommendation } from '../user-recommendation.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUserRecommendation for edit and NewUserRecommendationFormGroupInput for create.
 */
type UserRecommendationFormGroupInput = IUserRecommendation | PartialWithRequiredKeyOf<NewUserRecommendation>;

type UserRecommendationFormDefaults = Pick<NewUserRecommendation, 'id'>;

type UserRecommendationFormGroupContent = {
  id: FormControl<IUserRecommendation['id'] | NewUserRecommendation['id']>;
  reason: FormControl<IUserRecommendation['reason']>;
};

export type UserRecommendationFormGroup = FormGroup<UserRecommendationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UserRecommendationFormService {
  createUserRecommendationFormGroup(userRecommendation: UserRecommendationFormGroupInput = { id: null }): UserRecommendationFormGroup {
    const userRecommendationRawValue = {
      ...this.getFormDefaults(),
      ...userRecommendation,
    };
    return new FormGroup<UserRecommendationFormGroupContent>({
      id: new FormControl(
        { value: userRecommendationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      reason: new FormControl(userRecommendationRawValue.reason),
    });
  }

  getUserRecommendation(form: UserRecommendationFormGroup): IUserRecommendation | NewUserRecommendation {
    return form.getRawValue() as IUserRecommendation | NewUserRecommendation;
  }

  resetForm(form: UserRecommendationFormGroup, userRecommendation: UserRecommendationFormGroupInput): void {
    const userRecommendationRawValue = { ...this.getFormDefaults(), ...userRecommendation };
    form.reset(
      {
        ...userRecommendationRawValue,
        id: { value: userRecommendationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): UserRecommendationFormDefaults {
    return {
      id: null,
    };
  }
}
