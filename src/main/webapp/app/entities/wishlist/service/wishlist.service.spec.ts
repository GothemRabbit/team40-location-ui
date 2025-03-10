import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IWishlist } from '../wishlist.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../wishlist.test-samples';

import { WishlistService } from './wishlist.service';

const requireRestSample: IWishlist = {
  ...sampleWithRequiredData,
};

describe('Wishlist Service', () => {
  let service: WishlistService;
  let httpMock: HttpTestingController;
  let expectedResult: IWishlist | IWishlist[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(WishlistService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Wishlist', () => {
      const wishlist = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(wishlist).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Wishlist', () => {
      const wishlist = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(wishlist).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Wishlist', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Wishlist', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Wishlist', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addWishlistToCollectionIfMissing', () => {
      it('should add a Wishlist to an empty array', () => {
        const wishlist: IWishlist = sampleWithRequiredData;
        expectedResult = service.addWishlistToCollectionIfMissing([], wishlist);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(wishlist);
      });

      it('should not add a Wishlist to an array that contains it', () => {
        const wishlist: IWishlist = sampleWithRequiredData;
        const wishlistCollection: IWishlist[] = [
          {
            ...wishlist,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addWishlistToCollectionIfMissing(wishlistCollection, wishlist);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Wishlist to an array that doesn't contain it", () => {
        const wishlist: IWishlist = sampleWithRequiredData;
        const wishlistCollection: IWishlist[] = [sampleWithPartialData];
        expectedResult = service.addWishlistToCollectionIfMissing(wishlistCollection, wishlist);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(wishlist);
      });

      it('should add only unique Wishlist to an array', () => {
        const wishlistArray: IWishlist[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const wishlistCollection: IWishlist[] = [sampleWithRequiredData];
        expectedResult = service.addWishlistToCollectionIfMissing(wishlistCollection, ...wishlistArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const wishlist: IWishlist = sampleWithRequiredData;
        const wishlist2: IWishlist = sampleWithPartialData;
        expectedResult = service.addWishlistToCollectionIfMissing([], wishlist, wishlist2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(wishlist);
        expect(expectedResult).toContain(wishlist2);
      });

      it('should accept null and undefined values', () => {
        const wishlist: IWishlist = sampleWithRequiredData;
        expectedResult = service.addWishlistToCollectionIfMissing([], null, wishlist, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(wishlist);
      });

      it('should return initial array if no Wishlist is added', () => {
        const wishlistCollection: IWishlist[] = [sampleWithRequiredData];
        expectedResult = service.addWishlistToCollectionIfMissing(wishlistCollection, undefined, null);
        expect(expectedResult).toEqual(wishlistCollection);
      });
    });

    describe('compareWishlist', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareWishlist(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareWishlist(entity1, entity2);
        const compareResult2 = service.compareWishlist(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareWishlist(entity1, entity2);
        const compareResult2 = service.compareWishlist(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareWishlist(entity1, entity2);
        const compareResult2 = service.compareWishlist(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
