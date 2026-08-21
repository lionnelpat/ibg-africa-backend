import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ISousMatiere } from '../sous-matiere.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../sous-matiere.test-samples';

import { SousMatiereService } from './sous-matiere.service';

const requireRestSample: ISousMatiere = {
  ...sampleWithRequiredData,
};

describe('SousMatiere Service', () => {
  let service: SousMatiereService;
  let httpMock: HttpTestingController;
  let expectedResult: ISousMatiere | ISousMatiere[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(SousMatiereService);
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

    it('should create a SousMatiere', () => {
      const sousMatiere = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(sousMatiere).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SousMatiere', () => {
      const sousMatiere = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(sousMatiere).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SousMatiere', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SousMatiere', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SousMatiere', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSousMatiereToCollectionIfMissing', () => {
      it('should add a SousMatiere to an empty array', () => {
        const sousMatiere: ISousMatiere = sampleWithRequiredData;
        expectedResult = service.addSousMatiereToCollectionIfMissing([], sousMatiere);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sousMatiere);
      });

      it('should not add a SousMatiere to an array that contains it', () => {
        const sousMatiere: ISousMatiere = sampleWithRequiredData;
        const sousMatiereCollection: ISousMatiere[] = [
          {
            ...sousMatiere,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSousMatiereToCollectionIfMissing(sousMatiereCollection, sousMatiere);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SousMatiere to an array that doesn't contain it", () => {
        const sousMatiere: ISousMatiere = sampleWithRequiredData;
        const sousMatiereCollection: ISousMatiere[] = [sampleWithPartialData];
        expectedResult = service.addSousMatiereToCollectionIfMissing(sousMatiereCollection, sousMatiere);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sousMatiere);
      });

      it('should add only unique SousMatiere to an array', () => {
        const sousMatiereArray: ISousMatiere[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const sousMatiereCollection: ISousMatiere[] = [sampleWithRequiredData];
        expectedResult = service.addSousMatiereToCollectionIfMissing(sousMatiereCollection, ...sousMatiereArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const sousMatiere: ISousMatiere = sampleWithRequiredData;
        const sousMatiere2: ISousMatiere = sampleWithPartialData;
        expectedResult = service.addSousMatiereToCollectionIfMissing([], sousMatiere, sousMatiere2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sousMatiere);
        expect(expectedResult).toContain(sousMatiere2);
      });

      it('should accept null and undefined values', () => {
        const sousMatiere: ISousMatiere = sampleWithRequiredData;
        expectedResult = service.addSousMatiereToCollectionIfMissing([], null, sousMatiere, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sousMatiere);
      });

      it('should return initial array if no SousMatiere is added', () => {
        const sousMatiereCollection: ISousMatiere[] = [sampleWithRequiredData];
        expectedResult = service.addSousMatiereToCollectionIfMissing(sousMatiereCollection, undefined, null);
        expect(expectedResult).toEqual(sousMatiereCollection);
      });
    });

    describe('compareSousMatiere', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSousMatiere(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 5044 };
        const entity2 = null;

        const compareResult1 = service.compareSousMatiere(entity1, entity2);
        const compareResult2 = service.compareSousMatiere(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 5044 };
        const entity2 = { id: 7334 };

        const compareResult1 = service.compareSousMatiere(entity1, entity2);
        const compareResult2 = service.compareSousMatiere(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 5044 };
        const entity2 = { id: 5044 };

        const compareResult1 = service.compareSousMatiere(entity1, entity2);
        const compareResult2 = service.compareSousMatiere(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
