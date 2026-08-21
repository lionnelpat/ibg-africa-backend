import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IEvaluationPrevue } from '../evaluation-prevue.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../evaluation-prevue.test-samples';

import { EvaluationPrevueService, RestEvaluationPrevue } from './evaluation-prevue.service';

const requireRestSample: RestEvaluationPrevue = {
  ...sampleWithRequiredData,
  dateDebut: sampleWithRequiredData.dateDebut?.format(DATE_FORMAT),
  dateFin: sampleWithRequiredData.dateFin?.format(DATE_FORMAT),
};

describe('EvaluationPrevue Service', () => {
  let service: EvaluationPrevueService;
  let httpMock: HttpTestingController;
  let expectedResult: IEvaluationPrevue | IEvaluationPrevue[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(EvaluationPrevueService);
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

    it('should create a EvaluationPrevue', () => {
      const evaluationPrevue = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(evaluationPrevue).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a EvaluationPrevue', () => {
      const evaluationPrevue = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(evaluationPrevue).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a EvaluationPrevue', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of EvaluationPrevue', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a EvaluationPrevue', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addEvaluationPrevueToCollectionIfMissing', () => {
      it('should add a EvaluationPrevue to an empty array', () => {
        const evaluationPrevue: IEvaluationPrevue = sampleWithRequiredData;
        expectedResult = service.addEvaluationPrevueToCollectionIfMissing([], evaluationPrevue);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(evaluationPrevue);
      });

      it('should not add a EvaluationPrevue to an array that contains it', () => {
        const evaluationPrevue: IEvaluationPrevue = sampleWithRequiredData;
        const evaluationPrevueCollection: IEvaluationPrevue[] = [
          {
            ...evaluationPrevue,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEvaluationPrevueToCollectionIfMissing(evaluationPrevueCollection, evaluationPrevue);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a EvaluationPrevue to an array that doesn't contain it", () => {
        const evaluationPrevue: IEvaluationPrevue = sampleWithRequiredData;
        const evaluationPrevueCollection: IEvaluationPrevue[] = [sampleWithPartialData];
        expectedResult = service.addEvaluationPrevueToCollectionIfMissing(evaluationPrevueCollection, evaluationPrevue);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(evaluationPrevue);
      });

      it('should add only unique EvaluationPrevue to an array', () => {
        const evaluationPrevueArray: IEvaluationPrevue[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const evaluationPrevueCollection: IEvaluationPrevue[] = [sampleWithRequiredData];
        expectedResult = service.addEvaluationPrevueToCollectionIfMissing(evaluationPrevueCollection, ...evaluationPrevueArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const evaluationPrevue: IEvaluationPrevue = sampleWithRequiredData;
        const evaluationPrevue2: IEvaluationPrevue = sampleWithPartialData;
        expectedResult = service.addEvaluationPrevueToCollectionIfMissing([], evaluationPrevue, evaluationPrevue2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(evaluationPrevue);
        expect(expectedResult).toContain(evaluationPrevue2);
      });

      it('should accept null and undefined values', () => {
        const evaluationPrevue: IEvaluationPrevue = sampleWithRequiredData;
        expectedResult = service.addEvaluationPrevueToCollectionIfMissing([], null, evaluationPrevue, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(evaluationPrevue);
      });

      it('should return initial array if no EvaluationPrevue is added', () => {
        const evaluationPrevueCollection: IEvaluationPrevue[] = [sampleWithRequiredData];
        expectedResult = service.addEvaluationPrevueToCollectionIfMissing(evaluationPrevueCollection, undefined, null);
        expect(expectedResult).toEqual(evaluationPrevueCollection);
      });
    });

    describe('compareEvaluationPrevue', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEvaluationPrevue(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 11034 };
        const entity2 = null;

        const compareResult1 = service.compareEvaluationPrevue(entity1, entity2);
        const compareResult2 = service.compareEvaluationPrevue(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 11034 };
        const entity2 = { id: 20754 };

        const compareResult1 = service.compareEvaluationPrevue(entity1, entity2);
        const compareResult2 = service.compareEvaluationPrevue(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 11034 };
        const entity2 = { id: 11034 };

        const compareResult1 = service.compareEvaluationPrevue(entity1, entity2);
        const compareResult2 = service.compareEvaluationPrevue(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
