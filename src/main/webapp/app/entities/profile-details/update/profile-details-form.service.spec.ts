import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../profile-details.test-samples';

import { ProfileDetailsFormService } from './profile-details-form.service';

describe('ProfileDetails Form Service', () => {
  let service: ProfileDetailsFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProfileDetailsFormService);
  });

  describe('Service methods', () => {
    describe('createProfileDetailsFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createProfileDetailsFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            bioImage: expect.any(Object),
            userName: expect.any(Object),
            birthDate: expect.any(Object),
            user: expect.any(Object),
            locations: expect.any(Object),
            conversations: expect.any(Object),
          }),
        );
      });

      it('passing IProfileDetails should create a new form with FormGroup', () => {
        const formGroup = service.createProfileDetailsFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            bioImage: expect.any(Object),
            userName: expect.any(Object),
            birthDate: expect.any(Object),
            user: expect.any(Object),
            locations: expect.any(Object),
            conversations: expect.any(Object),
          }),
        );
      });
    });

    describe('getProfileDetails', () => {
      it('should return NewProfileDetails for default ProfileDetails initial value', () => {
        const formGroup = service.createProfileDetailsFormGroup(sampleWithNewData);

        const profileDetails = service.getProfileDetails(formGroup) as any;

        expect(profileDetails).toMatchObject(sampleWithNewData);
      });

      it('should return NewProfileDetails for empty ProfileDetails initial value', () => {
        const formGroup = service.createProfileDetailsFormGroup();

        const profileDetails = service.getProfileDetails(formGroup) as any;

        expect(profileDetails).toMatchObject({});
      });

      it('should return IProfileDetails', () => {
        const formGroup = service.createProfileDetailsFormGroup(sampleWithRequiredData);

        const profileDetails = service.getProfileDetails(formGroup) as any;

        expect(profileDetails).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IProfileDetails should not enable id FormControl', () => {
        const formGroup = service.createProfileDetailsFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewProfileDetails should disable id FormControl', () => {
        const formGroup = service.createProfileDetailsFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
