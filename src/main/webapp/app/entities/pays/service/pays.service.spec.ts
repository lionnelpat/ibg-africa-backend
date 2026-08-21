import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IPays } from '../pays.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../pays.test-samples';

import { PaysService } from './pays.service';

const requireRestSample: IPays = {
  ...sampleWithRequiredData,
};

describe('Pays Service', () => {
  let service: PaysService;
  let httpMock: HttpTestingController;
  let expectedResult: IPays | IPays[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PaysService);
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

    it('should create a Pays', () => {
      const pays = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(pays).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Pays', () => {
      const pays = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(pays).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Pays', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Pays', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Pays', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPaysToCollectionIfMissing', () => {
      it('should add a Pays to an empty array', () => {
        const pays: IPays = sampleWithRequiredData;
        expectedResult = service.addPaysToCollectionIfMissing([], pays);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pays);
      });

      it('should not add a Pays to an array that contains it', () => {
        const pays: IPays = sampleWithRequiredData;
        const paysCollection: IPays[] = [
          {
            ...pays,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPaysToCollectionIfMissing(paysCollection, pays);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Pays to an array that doesn't contain it", () => {
        const pays: IPays = sampleWithRequiredData;
        const paysCollection: IPays[] = [sampleWithPartialData];
        expectedResult = service.addPaysToCollectionIfMissing(paysCollection, pays);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pays);
      });

      it('should add only unique Pays to an array', () => {
        const paysArray: IPays[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const paysCollection: IPays[] = [sampleWithRequiredData];
        expectedResult = service.addPaysToCollectionIfMissing(paysCollection, ...paysArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const pays: IPays = sampleWithRequiredData;
        const pays2: IPays = sampleWithPartialData;
        expectedResult = service.addPaysToCollectionIfMissing([], pays, pays2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pays);
        expect(expectedResult).toContain(pays2);
      });

      it('should accept null and undefined values', () => {
        const pays: IPays = sampleWithRequiredData;
        expectedResult = service.addPaysToCollectionIfMissing([], null, pays, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pays);
      });

      it('should return initial array if no Pays is added', () => {
        const paysCollection: IPays[] = [sampleWithRequiredData];
        expectedResult = service.addPaysToCollectionIfMissing(paysCollection, undefined, null);
        expect(expectedResult).toEqual(paysCollection);
      });
    });

    describe('comparePays', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePays(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 21471 };
        const entity2 = null;

        const compareResult1 = service.comparePays(entity1, entity2);
        const compareResult2 = service.comparePays(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 21471 };
        const entity2 = { id: 30122 };

        const compareResult1 = service.comparePays(entity1, entity2);
        const compareResult2 = service.comparePays(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 21471 };
        const entity2 = { id: 21471 };

        const compareResult1 = service.comparePays(entity1, entity2);
        const compareResult2 = service.comparePays(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
