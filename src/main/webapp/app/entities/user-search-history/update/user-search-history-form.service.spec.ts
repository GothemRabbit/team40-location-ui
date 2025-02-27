import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../user-search-history.test-samples';

import { UserSearchHistoryFormService } from './user-search-history-form.service';

describe('UserSearchHistory Form Service', () => {
  let service: UserSearchHistoryFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UserSearchHistoryFormService);
  });

  describe('Service methods', () => {
    describe('createUserSearchHistoryFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createUserSearchHistoryFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            searchQuery: expect.any(Object),
            filters: expect.any(Object),
            searchDate: expect.any(Object),
          }),
        );
      });

      it('passing IUserSearchHistory should create a new form with FormGroup', () => {
        const formGroup = service.createUserSearchHistoryFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            searchQuery: expect.any(Object),
            filters: expect.any(Object),
            searchDate: expect.any(Object),
          }),
        );
      });
    });

    describe('getUserSearchHistory', () => {
      it('should return NewUserSearchHistory for default UserSearchHistory initial value', () => {
        const formGroup = service.createUserSearchHistoryFormGroup(sampleWithNewData);

        const userSearchHistory = service.getUserSearchHistory(formGroup) as any;

        expect(userSearchHistory).toMatchObject(sampleWithNewData);
      });

      it('should return NewUserSearchHistory for empty UserSearchHistory initial value', () => {
        const formGroup = service.createUserSearchHistoryFormGroup();

        const userSearchHistory = service.getUserSearchHistory(formGroup) as any;

        expect(userSearchHistory).toMatchObject({});
      });

      it('should return IUserSearchHistory', () => {
        const formGroup = service.createUserSearchHistoryFormGroup(sampleWithRequiredData);

        const userSearchHistory = service.getUserSearchHistory(formGroup) as any;

        expect(userSearchHistory).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IUserSearchHistory should not enable id FormControl', () => {
        const formGroup = service.createUserSearchHistoryFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewUserSearchHistory should disable id FormControl', () => {
        const formGroup = service.createUserSearchHistoryFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
