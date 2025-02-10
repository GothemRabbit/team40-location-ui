import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
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

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IUserDetails | NewUserDetails> = Omit<T, 'lastActive'> & {
  lastActive?: string | null;
};

type UserDetailsFormRawValue = FormValueOf<IUserDetails>;

type NewUserDetailsFormRawValue = FormValueOf<NewUserDetails>;

type UserDetailsFormDefaults = Pick<NewUserDetails, 'id' | 'lastActive' | 'conversations'>;

type UserDetailsFormGroupContent = {
  id: FormControl<UserDetailsFormRawValue['id'] | NewUserDetails['id']>;
  firstName: FormControl<UserDetailsFormRawValue['firstName']>;
  lastName: FormControl<UserDetailsFormRawValue['lastName']>;
  email: FormControl<UserDetailsFormRawValue['email']>;
  profilePic: FormControl<UserDetailsFormRawValue['profilePic']>;
  profilePicContentType: FormControl<UserDetailsFormRawValue['profilePicContentType']>;
  lastActive: FormControl<UserDetailsFormRawValue['lastActive']>;
  conversations: FormControl<UserDetailsFormRawValue['conversations']>;
};

export type UserDetailsFormGroup = FormGroup<UserDetailsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UserDetailsFormService {
  createUserDetailsFormGroup(userDetails: UserDetailsFormGroupInput = { id: null }): UserDetailsFormGroup {
    const userDetailsRawValue = this.convertUserDetailsToUserDetailsRawValue({
      ...this.getFormDefaults(),
      ...userDetails,
    });
    return new FormGroup<UserDetailsFormGroupContent>({
      id: new FormControl(
        { value: userDetailsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      firstName: new FormControl(userDetailsRawValue.firstName, {
        validators: [Validators.required],
      }),
      lastName: new FormControl(userDetailsRawValue.lastName, {
        validators: [Validators.required],
      }),
      email: new FormControl(userDetailsRawValue.email, {
        validators: [Validators.required],
      }),
      profilePic: new FormControl(userDetailsRawValue.profilePic),
      profilePicContentType: new FormControl(userDetailsRawValue.profilePicContentType),
      lastActive: new FormControl(userDetailsRawValue.lastActive, {
        validators: [Validators.required],
      }),
      conversations: new FormControl(userDetailsRawValue.conversations ?? []),
    });
  }

  getUserDetails(form: UserDetailsFormGroup): IUserDetails | NewUserDetails {
    return this.convertUserDetailsRawValueToUserDetails(form.getRawValue() as UserDetailsFormRawValue | NewUserDetailsFormRawValue);
  }

  resetForm(form: UserDetailsFormGroup, userDetails: UserDetailsFormGroupInput): void {
    const userDetailsRawValue = this.convertUserDetailsToUserDetailsRawValue({ ...this.getFormDefaults(), ...userDetails });
    form.reset(
      {
        ...userDetailsRawValue,
        id: { value: userDetailsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): UserDetailsFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      lastActive: currentTime,
      conversations: [],
    };
  }

  private convertUserDetailsRawValueToUserDetails(
    rawUserDetails: UserDetailsFormRawValue | NewUserDetailsFormRawValue,
  ): IUserDetails | NewUserDetails {
    return {
      ...rawUserDetails,
      lastActive: dayjs(rawUserDetails.lastActive, DATE_TIME_FORMAT),
    };
  }

  private convertUserDetailsToUserDetailsRawValue(
    userDetails: IUserDetails | (Partial<NewUserDetails> & UserDetailsFormDefaults),
  ): UserDetailsFormRawValue | PartialWithRequiredKeyOf<NewUserDetailsFormRawValue> {
    return {
      ...userDetails,
      lastActive: userDetails.lastActive ? userDetails.lastActive.format(DATE_TIME_FORMAT) : undefined,
      conversations: userDetails.conversations ?? [],
    };
  }
}
