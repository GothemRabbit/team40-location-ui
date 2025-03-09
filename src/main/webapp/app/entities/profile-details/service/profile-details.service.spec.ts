import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IProfileDetails } from '../profile-details.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../profile-details.test-samples';

import { ProfileDetailsService, RestProfileDetails } from './profile-details.service';

const requireRestSample: RestProfileDetails = {
  ...sampleWithRequiredData,
  birthDate: sampleWithRequiredData.birthDate?.format(DATE_FORMAT),
};

describe('ProfileDetails Service', () => {
  let service: ProfileDetailsService;
  let httpMock: HttpTestingController;
  let expectedResult: IProfileDetails | IProfileDetails[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ProfileDetailsService);
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

    it('should create a ProfileDetails', () => {
      const profileDetails = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(profileDetails).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ProfileDetails', () => {
      const profileDetails = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(profileDetails).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ProfileDetails', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ProfileDetails', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ProfileDetails', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addProfileDetailsToCollectionIfMissing', () => {
      it('should add a ProfileDetails to an empty array', () => {
        const profileDetails: IProfileDetails = sampleWithRequiredData;
        expectedResult = service.addProfileDetailsToCollectionIfMissing([], profileDetails);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(profileDetails);
      });

      it('should not add a ProfileDetails to an array that contains it', () => {
        const profileDetails: IProfileDetails = sampleWithRequiredData;
        const profileDetailsCollection: IProfileDetails[] = [
          {
            ...profileDetails,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addProfileDetailsToCollectionIfMissing(profileDetailsCollection, profileDetails);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ProfileDetails to an array that doesn't contain it", () => {
        const profileDetails: IProfileDetails = sampleWithRequiredData;
        const profileDetailsCollection: IProfileDetails[] = [sampleWithPartialData];
        expectedResult = service.addProfileDetailsToCollectionIfMissing(profileDetailsCollection, profileDetails);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(profileDetails);
      });

      it('should add only unique ProfileDetails to an array', () => {
        const profileDetailsArray: IProfileDetails[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const profileDetailsCollection: IProfileDetails[] = [sampleWithRequiredData];
        expectedResult = service.addProfileDetailsToCollectionIfMissing(profileDetailsCollection, ...profileDetailsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const profileDetails: IProfileDetails = sampleWithRequiredData;
        const profileDetails2: IProfileDetails = sampleWithPartialData;
        expectedResult = service.addProfileDetailsToCollectionIfMissing([], profileDetails, profileDetails2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(profileDetails);
        expect(expectedResult).toContain(profileDetails2);
      });

      it('should accept null and undefined values', () => {
        const profileDetails: IProfileDetails = sampleWithRequiredData;
        expectedResult = service.addProfileDetailsToCollectionIfMissing([], null, profileDetails, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(profileDetails);
      });

      it('should return initial array if no ProfileDetails is added', () => {
        const profileDetailsCollection: IProfileDetails[] = [sampleWithRequiredData];
        expectedResult = service.addProfileDetailsToCollectionIfMissing(profileDetailsCollection, undefined, null);
        expect(expectedResult).toEqual(profileDetailsCollection);
      });
    });

    describe('compareProfileDetails', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareProfileDetails(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareProfileDetails(entity1, entity2);
        const compareResult2 = service.compareProfileDetails(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareProfileDetails(entity1, entity2);
        const compareResult2 = service.compareProfileDetails(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareProfileDetails(entity1, entity2);
        const compareResult2 = service.compareProfileDetails(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
