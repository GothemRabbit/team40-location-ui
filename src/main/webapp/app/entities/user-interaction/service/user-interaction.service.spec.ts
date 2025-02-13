import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IUserInteraction } from '../user-interaction.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../user-interaction.test-samples';

import { RestUserInteraction, UserInteractionService } from './user-interaction.service';

const requireRestSample: RestUserInteraction = {
  ...sampleWithRequiredData,
  interactionDate: sampleWithRequiredData.interactionDate?.toJSON(),
};

describe('UserInteraction Service', () => {
  let service: UserInteractionService;
  let httpMock: HttpTestingController;
  let expectedResult: IUserInteraction | IUserInteraction[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(UserInteractionService);
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

    it('should create a UserInteraction', () => {
      const userInteraction = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(userInteraction).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a UserInteraction', () => {
      const userInteraction = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(userInteraction).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a UserInteraction', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of UserInteraction', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a UserInteraction', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addUserInteractionToCollectionIfMissing', () => {
      it('should add a UserInteraction to an empty array', () => {
        const userInteraction: IUserInteraction = sampleWithRequiredData;
        expectedResult = service.addUserInteractionToCollectionIfMissing([], userInteraction);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(userInteraction);
      });

      it('should not add a UserInteraction to an array that contains it', () => {
        const userInteraction: IUserInteraction = sampleWithRequiredData;
        const userInteractionCollection: IUserInteraction[] = [
          {
            ...userInteraction,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addUserInteractionToCollectionIfMissing(userInteractionCollection, userInteraction);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a UserInteraction to an array that doesn't contain it", () => {
        const userInteraction: IUserInteraction = sampleWithRequiredData;
        const userInteractionCollection: IUserInteraction[] = [sampleWithPartialData];
        expectedResult = service.addUserInteractionToCollectionIfMissing(userInteractionCollection, userInteraction);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(userInteraction);
      });

      it('should add only unique UserInteraction to an array', () => {
        const userInteractionArray: IUserInteraction[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const userInteractionCollection: IUserInteraction[] = [sampleWithRequiredData];
        expectedResult = service.addUserInteractionToCollectionIfMissing(userInteractionCollection, ...userInteractionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const userInteraction: IUserInteraction = sampleWithRequiredData;
        const userInteraction2: IUserInteraction = sampleWithPartialData;
        expectedResult = service.addUserInteractionToCollectionIfMissing([], userInteraction, userInteraction2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(userInteraction);
        expect(expectedResult).toContain(userInteraction2);
      });

      it('should accept null and undefined values', () => {
        const userInteraction: IUserInteraction = sampleWithRequiredData;
        expectedResult = service.addUserInteractionToCollectionIfMissing([], null, userInteraction, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(userInteraction);
      });

      it('should return initial array if no UserInteraction is added', () => {
        const userInteractionCollection: IUserInteraction[] = [sampleWithRequiredData];
        expectedResult = service.addUserInteractionToCollectionIfMissing(userInteractionCollection, undefined, null);
        expect(expectedResult).toEqual(userInteractionCollection);
      });
    });

    describe('compareUserInteraction', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareUserInteraction(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareUserInteraction(entity1, entity2);
        const compareResult2 = service.compareUserInteraction(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareUserInteraction(entity1, entity2);
        const compareResult2 = service.compareUserInteraction(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareUserInteraction(entity1, entity2);
        const compareResult2 = service.compareUserInteraction(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
