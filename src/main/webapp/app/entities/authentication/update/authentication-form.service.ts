import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IAuthentication, NewAuthentication } from '../authentication.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAuthentication for edit and NewAuthenticationFormGroupInput for create.
 */
type AuthenticationFormGroupInput = IAuthentication | PartialWithRequiredKeyOf<NewAuthentication>;

type AuthenticationFormDefaults = Pick<NewAuthentication, 'id'>;

type AuthenticationFormGroupContent = {
  id: FormControl<IAuthentication['id'] | NewAuthentication['id']>;
  password: FormControl<IAuthentication['password']>;
};

export type AuthenticationFormGroup = FormGroup<AuthenticationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AuthenticationFormService {
  createAuthenticationFormGroup(authentication: AuthenticationFormGroupInput = { id: null }): AuthenticationFormGroup {
    const authenticationRawValue = {
      ...this.getFormDefaults(),
      ...authentication,
    };
    return new FormGroup<AuthenticationFormGroupContent>({
      id: new FormControl(
        { value: authenticationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      password: new FormControl(authenticationRawValue.password, {
        validators: [Validators.required],
      }),
    });
  }

  getAuthentication(form: AuthenticationFormGroup): IAuthentication | NewAuthentication {
    return form.getRawValue() as IAuthentication | NewAuthentication;
  }

  resetForm(form: AuthenticationFormGroup, authentication: AuthenticationFormGroupInput): void {
    const authenticationRawValue = { ...this.getFormDefaults(), ...authentication };
    form.reset(
      {
        ...authenticationRawValue,
        id: { value: authenticationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AuthenticationFormDefaults {
    return {
      id: null,
    };
  }
}
