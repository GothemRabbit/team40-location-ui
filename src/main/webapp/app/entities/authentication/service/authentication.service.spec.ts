import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IAuthentication } from '../authentication.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../authentication.test-samples';

import { AuthenticationService } from './authentication.service';

const requireRestSample: IAuthentication = {
  ...sampleWithRequiredData,
};

describe('Authentication Service', () => {
  let service: AuthenticationService;
  let httpMock: HttpTestingController;
  let expectedResult: IAuthentication | IAuthentication[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(AuthenticationService);
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

    it('should create a Authentication', () => {
      const authentication = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(authentication).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Authentication', () => {
      const authentication = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(authentication).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Authentication', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Authentication', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Authentication', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAuthenticationToCollectionIfMissing', () => {
      it('should add a Authentication to an empty array', () => {
        const authentication: IAuthentication = sampleWithRequiredData;
        expectedResult = service.addAuthenticationToCollectionIfMissing([], authentication);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(authentication);
      });

      it('should not add a Authentication to an array that contains it', () => {
        const authentication: IAuthentication = sampleWithRequiredData;
        const authenticationCollection: IAuthentication[] = [
          {
            ...authentication,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAuthenticationToCollectionIfMissing(authenticationCollection, authentication);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Authentication to an array that doesn't contain it", () => {
        const authentication: IAuthentication = sampleWithRequiredData;
        const authenticationCollection: IAuthentication[] = [sampleWithPartialData];
        expectedResult = service.addAuthenticationToCollectionIfMissing(authenticationCollection, authentication);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(authentication);
      });

      it('should add only unique Authentication to an array', () => {
        const authenticationArray: IAuthentication[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const authenticationCollection: IAuthentication[] = [sampleWithRequiredData];
        expectedResult = service.addAuthenticationToCollectionIfMissing(authenticationCollection, ...authenticationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const authentication: IAuthentication = sampleWithRequiredData;
        const authentication2: IAuthentication = sampleWithPartialData;
        expectedResult = service.addAuthenticationToCollectionIfMissing([], authentication, authentication2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(authentication);
        expect(expectedResult).toContain(authentication2);
      });

      it('should accept null and undefined values', () => {
        const authentication: IAuthentication = sampleWithRequiredData;
        expectedResult = service.addAuthenticationToCollectionIfMissing([], null, authentication, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(authentication);
      });

      it('should return initial array if no Authentication is added', () => {
        const authenticationCollection: IAuthentication[] = [sampleWithRequiredData];
        expectedResult = service.addAuthenticationToCollectionIfMissing(authenticationCollection, undefined, null);
        expect(expectedResult).toEqual(authenticationCollection);
      });
    });

    describe('compareAuthentication', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAuthentication(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareAuthentication(entity1, entity2);
        const compareResult2 = service.compareAuthentication(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareAuthentication(entity1, entity2);
        const compareResult2 = service.compareAuthentication(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareAuthentication(entity1, entity2);
        const compareResult2 = service.compareAuthentication(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
