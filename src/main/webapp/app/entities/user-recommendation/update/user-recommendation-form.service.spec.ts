import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../user-recommendation.test-samples';

import { UserRecommendationFormService } from './user-recommendation-form.service';

describe('UserRecommendation Form Service', () => {
  let service: UserRecommendationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UserRecommendationFormService);
  });

  describe('Service methods', () => {
    describe('createUserRecommendationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createUserRecommendationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            reason: expect.any(Object),
          }),
        );
      });

      it('passing IUserRecommendation should create a new form with FormGroup', () => {
        const formGroup = service.createUserRecommendationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            reason: expect.any(Object),
          }),
        );
      });
    });

    describe('getUserRecommendation', () => {
      it('should return NewUserRecommendation for default UserRecommendation initial value', () => {
        const formGroup = service.createUserRecommendationFormGroup(sampleWithNewData);

        const userRecommendation = service.getUserRecommendation(formGroup) as any;

        expect(userRecommendation).toMatchObject(sampleWithNewData);
      });

      it('should return NewUserRecommendation for empty UserRecommendation initial value', () => {
        const formGroup = service.createUserRecommendationFormGroup();

        const userRecommendation = service.getUserRecommendation(formGroup) as any;

        expect(userRecommendation).toMatchObject({});
      });

      it('should return IUserRecommendation', () => {
        const formGroup = service.createUserRecommendationFormGroup(sampleWithRequiredData);

        const userRecommendation = service.getUserRecommendation(formGroup) as any;

        expect(userRecommendation).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IUserRecommendation should not enable id FormControl', () => {
        const formGroup = service.createUserRecommendationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewUserRecommendation should disable id FormControl', () => {
        const formGroup = service.createUserRecommendationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
