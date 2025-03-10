import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ILocation, NewLocation } from '../location.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ILocation for edit and NewLocationFormGroupInput for create.
 */
type LocationFormGroupInput = ILocation | PartialWithRequiredKeyOf<NewLocation>;

type LocationFormDefaults = Pick<NewLocation, 'id' | 'profileDetails' | 'users'>;

type LocationFormGroupContent = {
  id: FormControl<ILocation['id'] | NewLocation['id']>;
  address: FormControl<ILocation['address']>;
  latitude: FormControl<ILocation['latitude']>;
  longitude: FormControl<ILocation['longitude']>;
  postcode: FormControl<ILocation['postcode']>;
  profileDetails: FormControl<ILocation['profileDetails']>;
  users: FormControl<ILocation['users']>;
};

export type LocationFormGroup = FormGroup<LocationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class LocationFormService {
  createLocationFormGroup(location: LocationFormGroupInput = { id: null }): LocationFormGroup {
    const locationRawValue = {
      ...this.getFormDefaults(),
      ...location,
    };
    return new FormGroup<LocationFormGroupContent>({
      id: new FormControl(
        { value: locationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      address: new FormControl(locationRawValue.address, {
        validators: [Validators.required],
      }),
      latitude: new FormControl(locationRawValue.latitude, {
        validators: [Validators.required],
      }),
      longitude: new FormControl(locationRawValue.longitude, {
        validators: [Validators.required],
      }),
      postcode: new FormControl(locationRawValue.postcode, {
        validators: [Validators.required],
      }),
      profileDetails: new FormControl(locationRawValue.profileDetails ?? []),
      users: new FormControl(locationRawValue.users ?? []),
    });
  }

  getLocation(form: LocationFormGroup): ILocation | NewLocation {
    return form.getRawValue() as ILocation | NewLocation;
  }

  resetForm(form: LocationFormGroup, location: LocationFormGroupInput): void {
    const locationRawValue = { ...this.getFormDefaults(), ...location };
    form.reset(
      {
        ...locationRawValue,
        id: { value: locationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): LocationFormDefaults {
    return {
      id: null,
      profileDetails: [],
      users: [],
    };
  }
}
