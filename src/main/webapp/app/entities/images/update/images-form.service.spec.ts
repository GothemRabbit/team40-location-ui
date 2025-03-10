import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../images.test-samples';

import { ImagesFormService } from './images-form.service';

describe('Images Form Service', () => {
  let service: ImagesFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ImagesFormService);
  });

  describe('Service methods', () => {
    describe('createImagesFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createImagesFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            images: expect.any(Object),
            item: expect.any(Object),
          }),
        );
      });

      it('passing IImages should create a new form with FormGroup', () => {
        const formGroup = service.createImagesFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            images: expect.any(Object),
            item: expect.any(Object),
          }),
        );
      });
    });

    describe('getImages', () => {
      it('should return NewImages for default Images initial value', () => {
        const formGroup = service.createImagesFormGroup(sampleWithNewData);

        const images = service.getImages(formGroup) as any;

        expect(images).toMatchObject(sampleWithNewData);
      });

      it('should return NewImages for empty Images initial value', () => {
        const formGroup = service.createImagesFormGroup();

        const images = service.getImages(formGroup) as any;

        expect(images).toMatchObject({});
      });

      it('should return IImages', () => {
        const formGroup = service.createImagesFormGroup(sampleWithRequiredData);

        const images = service.getImages(formGroup) as any;

        expect(images).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IImages should not enable id FormControl', () => {
        const formGroup = service.createImagesFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewImages should disable id FormControl', () => {
        const formGroup = service.createImagesFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
