import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IEvaluationRealisee } from '../evaluation-realisee.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../evaluation-realisee.test-samples';

import { EvaluationRealiseeService, RestEvaluationRealisee } from './evaluation-realisee.service';

const requireRestSample: RestEvaluationRealisee = {
  ...sampleWithRequiredData,
  dateDebut: sampleWithRequiredData.dateDebut?.format(DATE_FORMAT),
  dateFin: sampleWithRequiredData.dateFin?.format(DATE_FORMAT),
  saisieLe: sampleWithRequiredData.saisieLe?.toJSON(),
  valideeLe: sampleWithRequiredData.valideeLe?.toJSON(),
};

describe('EvaluationRealisee Service', () => {
  let service: EvaluationRealiseeService;
  let httpMock: HttpTestingController;
  let expectedResult: IEvaluationRealisee | IEvaluationRealisee[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(EvaluationRealiseeService);
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

    it('should create a EvaluationRealisee', () => {
      const evaluationRealisee = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(evaluationRealisee).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a EvaluationRealisee', () => {
      const evaluationRealisee = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(evaluationRealisee).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a EvaluationRealisee', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of EvaluationRealisee', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a EvaluationRealisee', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addEvaluationRealiseeToCollectionIfMissing', () => {
      it('should add a EvaluationRealisee to an empty array', () => {
        const evaluationRealisee: IEvaluationRealisee = sampleWithRequiredData;
        expectedResult = service.addEvaluationRealiseeToCollectionIfMissing([], evaluationRealisee);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(evaluationRealisee);
      });

      it('should not add a EvaluationRealisee to an array that contains it', () => {
        const evaluationRealisee: IEvaluationRealisee = sampleWithRequiredData;
        const evaluationRealiseeCollection: IEvaluationRealisee[] = [
          {
            ...evaluationRealisee,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEvaluationRealiseeToCollectionIfMissing(evaluationRealiseeCollection, evaluationRealisee);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a EvaluationRealisee to an array that doesn't contain it", () => {
        const evaluationRealisee: IEvaluationRealisee = sampleWithRequiredData;
        const evaluationRealiseeCollection: IEvaluationRealisee[] = [sampleWithPartialData];
        expectedResult = service.addEvaluationRealiseeToCollectionIfMissing(evaluationRealiseeCollection, evaluationRealisee);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(evaluationRealisee);
      });

      it('should add only unique EvaluationRealisee to an array', () => {
        const evaluationRealiseeArray: IEvaluationRealisee[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const evaluationRealiseeCollection: IEvaluationRealisee[] = [sampleWithRequiredData];
        expectedResult = service.addEvaluationRealiseeToCollectionIfMissing(evaluationRealiseeCollection, ...evaluationRealiseeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const evaluationRealisee: IEvaluationRealisee = sampleWithRequiredData;
        const evaluationRealisee2: IEvaluationRealisee = sampleWithPartialData;
        expectedResult = service.addEvaluationRealiseeToCollectionIfMissing([], evaluationRealisee, evaluationRealisee2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(evaluationRealisee);
        expect(expectedResult).toContain(evaluationRealisee2);
      });

      it('should accept null and undefined values', () => {
        const evaluationRealisee: IEvaluationRealisee = sampleWithRequiredData;
        expectedResult = service.addEvaluationRealiseeToCollectionIfMissing([], null, evaluationRealisee, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(evaluationRealisee);
      });

      it('should return initial array if no EvaluationRealisee is added', () => {
        const evaluationRealiseeCollection: IEvaluationRealisee[] = [sampleWithRequiredData];
        expectedResult = service.addEvaluationRealiseeToCollectionIfMissing(evaluationRealiseeCollection, undefined, null);
        expect(expectedResult).toEqual(evaluationRealiseeCollection);
      });
    });

    describe('compareEvaluationRealisee', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEvaluationRealisee(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 11421 };
        const entity2 = null;

        const compareResult1 = service.compareEvaluationRealisee(entity1, entity2);
        const compareResult2 = service.compareEvaluationRealisee(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 11421 };
        const entity2 = { id: 4126 };

        const compareResult1 = service.compareEvaluationRealisee(entity1, entity2);
        const compareResult2 = service.compareEvaluationRealisee(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 11421 };
        const entity2 = { id: 11421 };

        const compareResult1 = service.compareEvaluationRealisee(entity1, entity2);
        const compareResult2 = service.compareEvaluationRealisee(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
