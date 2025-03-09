import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IImages } from '../images.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../images.test-samples';

import { ImagesService } from './images.service';

const requireRestSample: IImages = {
  ...sampleWithRequiredData,
};

describe('Images Service', () => {
  let service: ImagesService;
  let httpMock: HttpTestingController;
  let expectedResult: IImages | IImages[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ImagesService);
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

    it('should create a Images', () => {
      const images = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(images).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Images', () => {
      const images = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(images).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Images', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Images', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Images', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addImagesToCollectionIfMissing', () => {
      it('should add a Images to an empty array', () => {
        const images: IImages = sampleWithRequiredData;
        expectedResult = service.addImagesToCollectionIfMissing([], images);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(images);
      });

      it('should not add a Images to an array that contains it', () => {
        const images: IImages = sampleWithRequiredData;
        const imagesCollection: IImages[] = [
          {
            ...images,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addImagesToCollectionIfMissing(imagesCollection, images);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Images to an array that doesn't contain it", () => {
        const images: IImages = sampleWithRequiredData;
        const imagesCollection: IImages[] = [sampleWithPartialData];
        expectedResult = service.addImagesToCollectionIfMissing(imagesCollection, images);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(images);
      });

      it('should add only unique Images to an array', () => {
        const imagesArray: IImages[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const imagesCollection: IImages[] = [sampleWithRequiredData];
        expectedResult = service.addImagesToCollectionIfMissing(imagesCollection, ...imagesArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const images: IImages = sampleWithRequiredData;
        const images2: IImages = sampleWithPartialData;
        expectedResult = service.addImagesToCollectionIfMissing([], images, images2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(images);
        expect(expectedResult).toContain(images2);
      });

      it('should accept null and undefined values', () => {
        const images: IImages = sampleWithRequiredData;
        expectedResult = service.addImagesToCollectionIfMissing([], null, images, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(images);
      });

      it('should return initial array if no Images is added', () => {
        const imagesCollection: IImages[] = [sampleWithRequiredData];
        expectedResult = service.addImagesToCollectionIfMissing(imagesCollection, undefined, null);
        expect(expectedResult).toEqual(imagesCollection);
      });
    });

    describe('compareImages', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareImages(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareImages(entity1, entity2);
        const compareResult2 = service.compareImages(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareImages(entity1, entity2);
        const compareResult2 = service.compareImages(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareImages(entity1, entity2);
        const compareResult2 = service.compareImages(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
