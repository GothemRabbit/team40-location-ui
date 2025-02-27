import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IUserRecommendation } from '../user-recommendation.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../user-recommendation.test-samples';

import { UserRecommendationService } from './user-recommendation.service';

const requireRestSample: IUserRecommendation = {
  ...sampleWithRequiredData,
};

describe('UserRecommendation Service', () => {
  let service: UserRecommendationService;
  let httpMock: HttpTestingController;
  let expectedResult: IUserRecommendation | IUserRecommendation[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(UserRecommendationService);
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

    it('should create a UserRecommendation', () => {
      const userRecommendation = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(userRecommendation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a UserRecommendation', () => {
      const userRecommendation = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(userRecommendation).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a UserRecommendation', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of UserRecommendation', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a UserRecommendation', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addUserRecommendationToCollectionIfMissing', () => {
      it('should add a UserRecommendation to an empty array', () => {
        const userRecommendation: IUserRecommendation = sampleWithRequiredData;
        expectedResult = service.addUserRecommendationToCollectionIfMissing([], userRecommendation);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(userRecommendation);
      });

      it('should not add a UserRecommendation to an array that contains it', () => {
        const userRecommendation: IUserRecommendation = sampleWithRequiredData;
        const userRecommendationCollection: IUserRecommendation[] = [
          {
            ...userRecommendation,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addUserRecommendationToCollectionIfMissing(userRecommendationCollection, userRecommendation);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a UserRecommendation to an array that doesn't contain it", () => {
        const userRecommendation: IUserRecommendation = sampleWithRequiredData;
        const userRecommendationCollection: IUserRecommendation[] = [sampleWithPartialData];
        expectedResult = service.addUserRecommendationToCollectionIfMissing(userRecommendationCollection, userRecommendation);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(userRecommendation);
      });

      it('should add only unique UserRecommendation to an array', () => {
        const userRecommendationArray: IUserRecommendation[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const userRecommendationCollection: IUserRecommendation[] = [sampleWithRequiredData];
        expectedResult = service.addUserRecommendationToCollectionIfMissing(userRecommendationCollection, ...userRecommendationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const userRecommendation: IUserRecommendation = sampleWithRequiredData;
        const userRecommendation2: IUserRecommendation = sampleWithPartialData;
        expectedResult = service.addUserRecommendationToCollectionIfMissing([], userRecommendation, userRecommendation2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(userRecommendation);
        expect(expectedResult).toContain(userRecommendation2);
      });

      it('should accept null and undefined values', () => {
        const userRecommendation: IUserRecommendation = sampleWithRequiredData;
        expectedResult = service.addUserRecommendationToCollectionIfMissing([], null, userRecommendation, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(userRecommendation);
      });

      it('should return initial array if no UserRecommendation is added', () => {
        const userRecommendationCollection: IUserRecommendation[] = [sampleWithRequiredData];
        expectedResult = service.addUserRecommendationToCollectionIfMissing(userRecommendationCollection, undefined, null);
        expect(expectedResult).toEqual(userRecommendationCollection);
      });
    });

    describe('compareUserRecommendation', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareUserRecommendation(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareUserRecommendation(entity1, entity2);
        const compareResult2 = service.compareUserRecommendation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareUserRecommendation(entity1, entity2);
        const compareResult2 = service.compareUserRecommendation(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareUserRecommendation(entity1, entity2);
        const compareResult2 = service.compareUserRecommendation(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
