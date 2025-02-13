import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IUserDetails, NewUserDetails } from '../user-details.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUserDetails for edit and NewUserDetailsFormGroupInput for create.
 */
type UserDetailsFormGroupInput = IUserDetails | PartialWithRequiredKeyOf<NewUserDetails>;

type UserDetailsFormDefaults = Pick<NewUserDetails, 'id'>;

type UserDetailsFormGroupContent = {
  id: FormControl<IUserDetails['id'] | NewUserDetails['id']>;
  bioImage: FormControl<IUserDetails['bioImage']>;
  bioImageContentType: FormControl<IUserDetails['bioImageContentType']>;
  userName: FormControl<IUserDetails['userName']>;
  firstName: FormControl<IUserDetails['firstName']>;
  lastName: FormControl<IUserDetails['lastName']>;
  gender: FormControl<IUserDetails['gender']>;
  birthDate: FormControl<IUserDetails['birthDate']>;
  email: FormControl<IUserDetails['email']>;
  phoneNumber: FormControl<IUserDetails['phoneNumber']>;
  preferences: FormControl<IUserDetails['preferences']>;
  rating: FormControl<IUserDetails['rating']>;
  address: FormControl<IUserDetails['address']>;
};

export type UserDetailsFormGroup = FormGroup<UserDetailsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UserDetailsFormService {
  createUserDetailsFormGroup(userDetails: UserDetailsFormGroupInput = { id: null }): UserDetailsFormGroup {
    const userDetailsRawValue = {
      ...this.getFormDefaults(),
      ...userDetails,
    };
    return new FormGroup<UserDetailsFormGroupContent>({
      id: new FormControl(
        { value: userDetailsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      bioImage: new FormControl(userDetailsRawValue.bioImage),
      bioImageContentType: new FormControl(userDetailsRawValue.bioImageContentType),
      userName: new FormControl(userDetailsRawValue.userName, {
        validators: [Validators.required],
      }),
      firstName: new FormControl(userDetailsRawValue.firstName, {
        validators: [Validators.required],
      }),
      lastName: new FormControl(userDetailsRawValue.lastName, {
        validators: [Validators.required],
      }),
      gender: new FormControl(userDetailsRawValue.gender),
      birthDate: new FormControl(userDetailsRawValue.birthDate),
      email: new FormControl(userDetailsRawValue.email, {
        validators: [Validators.required],
      }),
      phoneNumber: new FormControl(userDetailsRawValue.phoneNumber, {
        validators: [Validators.required],
      }),
      preferences: new FormControl(userDetailsRawValue.preferences),
      rating: new FormControl(userDetailsRawValue.rating, {
        validators: [Validators.required, Validators.min(1), Validators.max(5)],
      }),
      address: new FormControl(userDetailsRawValue.address, {
        validators: [Validators.required],
      }),
    });
  }

  getUserDetails(form: UserDetailsFormGroup): IUserDetails | NewUserDetails {
    return form.getRawValue() as IUserDetails | NewUserDetails;
  }

  resetForm(form: UserDetailsFormGroup, userDetails: UserDetailsFormGroupInput): void {
    const userDetailsRawValue = { ...this.getFormDefaults(), ...userDetails };
    form.reset(
      {
        ...userDetailsRawValue,
        id: { value: userDetailsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): UserDetailsFormDefaults {
    return {
      id: null,
    };
  }
}
