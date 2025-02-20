import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../user-interaction.test-samples';

import { UserInteractionFormService } from './user-interaction-form.service';

describe('UserInteraction Form Service', () => {
  let service: UserInteractionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UserInteractionFormService);
  });

  describe('Service methods', () => {
    describe('createUserInteractionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createUserInteractionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            type: expect.any(Object),
            details: expect.any(Object),
            interactionDate: expect.any(Object),
          }),
        );
      });

      it('passing IUserInteraction should create a new form with FormGroup', () => {
        const formGroup = service.createUserInteractionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            type: expect.any(Object),
            details: expect.any(Object),
            interactionDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getUserInteraction', () => {
      it('should return NewUserInteraction for default UserInteraction initial value', () => {
        const formGroup = service.createUserInteractionFormGroup(sampleWithNewData);

        const userInteraction = service.getUserInteraction(formGroup) as any;

        expect(userInteraction).toMatchObject(sampleWithNewData);
      });

      it('should return NewUserInteraction for empty UserInteraction initial value', () => {
        const formGroup = service.createUserInteractionFormGroup();

        const userInteraction = service.getUserInteraction(formGroup) as any;

        expect(userInteraction).toMatchObject({});
      });

      it('should return IUserInteraction', () => {
        const formGroup = service.createUserInteractionFormGroup(sampleWithRequiredData);

        const userInteraction = service.getUserInteraction(formGroup) as any;

        expect(userInteraction).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IUserInteraction should not enable id FormControl', () => {
        const formGroup = service.createUserInteractionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewUserInteraction should disable id FormControl', () => {
        const formGroup = service.createUserInteractionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
