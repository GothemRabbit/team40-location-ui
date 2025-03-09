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

type UserDetailsFormDefaults = Pick<NewUserDetails, 'id' | 'meetupLocations' | 'chats'>;

type UserDetailsFormGroupContent = {
  id: FormControl<IUserDetails['id'] | NewUserDetails['id']>;
  bioImage: FormControl<IUserDetails['bioImage']>;
  bioImageContentType: FormControl<IUserDetails['bioImageContentType']>;
  userName: FormControl<IUserDetails['userName']>;
  birthDate: FormControl<IUserDetails['birthDate']>;
  user: FormControl<IUserDetails['user']>;
  meetupLocations: FormControl<IUserDetails['meetupLocations']>;
  chats: FormControl<IUserDetails['chats']>;
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
        validators: [Validators.required, Validators.pattern('^\\S+$')],
      }),
      birthDate: new FormControl(userDetailsRawValue.birthDate),
      user: new FormControl(userDetailsRawValue.user),
      meetupLocations: new FormControl(userDetailsRawValue.meetupLocations ?? []),
      chats: new FormControl(userDetailsRawValue.chats ?? []),
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
      meetupLocations: [],
      chats: [],
    };
  }
}
