import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../authentication.test-samples';

import { AuthenticationFormService } from './authentication-form.service';

describe('Authentication Form Service', () => {
  let service: AuthenticationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AuthenticationFormService);
  });

  describe('Service methods', () => {
    describe('createAuthenticationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAuthenticationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            password: expect.any(Object),
          }),
        );
      });

      it('passing IAuthentication should create a new form with FormGroup', () => {
        const formGroup = service.createAuthenticationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            password: expect.any(Object),
          }),
        );
      });
    });

    describe('getAuthentication', () => {
      it('should return NewAuthentication for default Authentication initial value', () => {
        const formGroup = service.createAuthenticationFormGroup(sampleWithNewData);

        const authentication = service.getAuthentication(formGroup) as any;

        expect(authentication).toMatchObject(sampleWithNewData);
      });

      it('should return NewAuthentication for empty Authentication initial value', () => {
        const formGroup = service.createAuthenticationFormGroup();

        const authentication = service.getAuthentication(formGroup) as any;

        expect(authentication).toMatchObject({});
      });

      it('should return IAuthentication', () => {
        const formGroup = service.createAuthenticationFormGroup(sampleWithRequiredData);

        const authentication = service.getAuthentication(formGroup) as any;

        expect(authentication).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAuthentication should not enable id FormControl', () => {
        const formGroup = service.createAuthenticationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAuthentication should disable id FormControl', () => {
        const formGroup = service.createAuthenticationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
