import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../wishlist.test-samples';

import { WishlistFormService } from './wishlist-form.service';

describe('Wishlist Form Service', () => {
  let service: WishlistFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WishlistFormService);
  });

  describe('Service methods', () => {
    describe('createWishlistFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createWishlistFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            visibility: expect.any(Object),
            profileDetails: expect.any(Object),
            items: expect.any(Object),
            userDetails: expect.any(Object),
          }),
        );
      });

      it('passing IWishlist should create a new form with FormGroup', () => {
        const formGroup = service.createWishlistFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            visibility: expect.any(Object),
            profileDetails: expect.any(Object),
            items: expect.any(Object),
            userDetails: expect.any(Object),
          }),
        );
      });
    });

    describe('getWishlist', () => {
      it('should return NewWishlist for default Wishlist initial value', () => {
        const formGroup = service.createWishlistFormGroup(sampleWithNewData);

        const wishlist = service.getWishlist(formGroup) as any;

        expect(wishlist).toMatchObject(sampleWithNewData);
      });

      it('should return NewWishlist for empty Wishlist initial value', () => {
        const formGroup = service.createWishlistFormGroup();

        const wishlist = service.getWishlist(formGroup) as any;

        expect(wishlist).toMatchObject({});
      });

      it('should return IWishlist', () => {
        const formGroup = service.createWishlistFormGroup(sampleWithRequiredData);

        const wishlist = service.getWishlist(formGroup) as any;

        expect(wishlist).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IWishlist should not enable id FormControl', () => {
        const formGroup = service.createWishlistFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewWishlist should disable id FormControl', () => {
        const formGroup = service.createWishlistFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
