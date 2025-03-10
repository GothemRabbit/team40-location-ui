import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IProfileDetails, NewProfileDetails } from '../profile-details.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProfileDetails for edit and NewProfileDetailsFormGroupInput for create.
 */
type ProfileDetailsFormGroupInput = IProfileDetails | PartialWithRequiredKeyOf<NewProfileDetails>;

type ProfileDetailsFormDefaults = Pick<NewProfileDetails, 'id' | 'locations' | 'conversations'>;

type ProfileDetailsFormGroupContent = {
  id: FormControl<IProfileDetails['id'] | NewProfileDetails['id']>;
  bioImage: FormControl<IProfileDetails['bioImage']>;
  bioImageContentType: FormControl<IProfileDetails['bioImageContentType']>;
  userName: FormControl<IProfileDetails['userName']>;
  birthDate: FormControl<IProfileDetails['birthDate']>;
  user: FormControl<IProfileDetails['user']>;
  locations: FormControl<IProfileDetails['locations']>;
  conversations: FormControl<IProfileDetails['conversations']>;
};

export type ProfileDetailsFormGroup = FormGroup<ProfileDetailsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProfileDetailsFormService {
  createProfileDetailsFormGroup(profileDetails: ProfileDetailsFormGroupInput = { id: null }): ProfileDetailsFormGroup {
    const profileDetailsRawValue = {
      ...this.getFormDefaults(),
      ...profileDetails,
    };
    return new FormGroup<ProfileDetailsFormGroupContent>({
      id: new FormControl(
        { value: profileDetailsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      bioImage: new FormControl(profileDetailsRawValue.bioImage),
      bioImageContentType: new FormControl(profileDetailsRawValue.bioImageContentType),
      userName: new FormControl(profileDetailsRawValue.userName, {
        validators: [Validators.required, Validators.pattern('^\\S+$')],
      }),
      birthDate: new FormControl(profileDetailsRawValue.birthDate),
      user: new FormControl(profileDetailsRawValue.user),
      locations: new FormControl(profileDetailsRawValue.locations ?? []),
      conversations: new FormControl(profileDetailsRawValue.conversations ?? []),
    });
  }

  getProfileDetails(form: ProfileDetailsFormGroup): IProfileDetails | NewProfileDetails {
    return form.getRawValue() as IProfileDetails | NewProfileDetails;
  }

  resetForm(form: ProfileDetailsFormGroup, profileDetails: ProfileDetailsFormGroupInput): void {
    const profileDetailsRawValue = { ...this.getFormDefaults(), ...profileDetails };
    form.reset(
      {
        ...profileDetailsRawValue,
        id: { value: profileDetailsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ProfileDetailsFormDefaults {
    return {
      id: null,
      locations: [],
      conversations: [],
    };
  }
}
