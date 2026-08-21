import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IInscriptionCycle } from '../inscription-cycle.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../inscription-cycle.test-samples';

import { InscriptionCycleService, RestInscriptionCycle } from './inscription-cycle.service';

const requireRestSample: RestInscriptionCycle = {
  ...sampleWithRequiredData,
  dateInscription: sampleWithRequiredData.dateInscription?.format(DATE_FORMAT),
};

describe('InscriptionCycle Service', () => {
  let service: InscriptionCycleService;
  let httpMock: HttpTestingController;
  let expectedResult: IInscriptionCycle | IInscriptionCycle[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(InscriptionCycleService);
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

    it('should create a InscriptionCycle', () => {
      const inscriptionCycle = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(inscriptionCycle).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a InscriptionCycle', () => {
      const inscriptionCycle = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(inscriptionCycle).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a InscriptionCycle', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of InscriptionCycle', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a InscriptionCycle', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addInscriptionCycleToCollectionIfMissing', () => {
      it('should add a InscriptionCycle to an empty array', () => {
        const inscriptionCycle: IInscriptionCycle = sampleWithRequiredData;
        expectedResult = service.addInscriptionCycleToCollectionIfMissing([], inscriptionCycle);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(inscriptionCycle);
      });

      it('should not add a InscriptionCycle to an array that contains it', () => {
        const inscriptionCycle: IInscriptionCycle = sampleWithRequiredData;
        const inscriptionCycleCollection: IInscriptionCycle[] = [
          {
            ...inscriptionCycle,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addInscriptionCycleToCollectionIfMissing(inscriptionCycleCollection, inscriptionCycle);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a InscriptionCycle to an array that doesn't contain it", () => {
        const inscriptionCycle: IInscriptionCycle = sampleWithRequiredData;
        const inscriptionCycleCollection: IInscriptionCycle[] = [sampleWithPartialData];
        expectedResult = service.addInscriptionCycleToCollectionIfMissing(inscriptionCycleCollection, inscriptionCycle);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(inscriptionCycle);
      });

      it('should add only unique InscriptionCycle to an array', () => {
        const inscriptionCycleArray: IInscriptionCycle[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const inscriptionCycleCollection: IInscriptionCycle[] = [sampleWithRequiredData];
        expectedResult = service.addInscriptionCycleToCollectionIfMissing(inscriptionCycleCollection, ...inscriptionCycleArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const inscriptionCycle: IInscriptionCycle = sampleWithRequiredData;
        const inscriptionCycle2: IInscriptionCycle = sampleWithPartialData;
        expectedResult = service.addInscriptionCycleToCollectionIfMissing([], inscriptionCycle, inscriptionCycle2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(inscriptionCycle);
        expect(expectedResult).toContain(inscriptionCycle2);
      });

      it('should accept null and undefined values', () => {
        const inscriptionCycle: IInscriptionCycle = sampleWithRequiredData;
        expectedResult = service.addInscriptionCycleToCollectionIfMissing([], null, inscriptionCycle, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(inscriptionCycle);
      });

      it('should return initial array if no InscriptionCycle is added', () => {
        const inscriptionCycleCollection: IInscriptionCycle[] = [sampleWithRequiredData];
        expectedResult = service.addInscriptionCycleToCollectionIfMissing(inscriptionCycleCollection, undefined, null);
        expect(expectedResult).toEqual(inscriptionCycleCollection);
      });
    });

    describe('compareInscriptionCycle', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareInscriptionCycle(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 5654 };
        const entity2 = null;

        const compareResult1 = service.compareInscriptionCycle(entity1, entity2);
        const compareResult2 = service.compareInscriptionCycle(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 5654 };
        const entity2 = { id: 8825 };

        const compareResult1 = service.compareInscriptionCycle(entity1, entity2);
        const compareResult2 = service.compareInscriptionCycle(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 5654 };
        const entity2 = { id: 5654 };

        const compareResult1 = service.compareInscriptionCycle(entity1, entity2);
        const compareResult2 = service.compareInscriptionCycle(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
