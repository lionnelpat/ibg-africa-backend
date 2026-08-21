import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IHabilitationCycle } from '../habilitation-cycle.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../habilitation-cycle.test-samples';

import { HabilitationCycleService, RestHabilitationCycle } from './habilitation-cycle.service';

const requireRestSample: RestHabilitationCycle = {
  ...sampleWithRequiredData,
  dateDebut: sampleWithRequiredData.dateDebut?.format(DATE_FORMAT),
  dateFin: sampleWithRequiredData.dateFin?.format(DATE_FORMAT),
};

describe('HabilitationCycle Service', () => {
  let service: HabilitationCycleService;
  let httpMock: HttpTestingController;
  let expectedResult: IHabilitationCycle | IHabilitationCycle[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(HabilitationCycleService);
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

    it('should create a HabilitationCycle', () => {
      const habilitationCycle = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(habilitationCycle).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a HabilitationCycle', () => {
      const habilitationCycle = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(habilitationCycle).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a HabilitationCycle', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of HabilitationCycle', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a HabilitationCycle', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addHabilitationCycleToCollectionIfMissing', () => {
      it('should add a HabilitationCycle to an empty array', () => {
        const habilitationCycle: IHabilitationCycle = sampleWithRequiredData;
        expectedResult = service.addHabilitationCycleToCollectionIfMissing([], habilitationCycle);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(habilitationCycle);
      });

      it('should not add a HabilitationCycle to an array that contains it', () => {
        const habilitationCycle: IHabilitationCycle = sampleWithRequiredData;
        const habilitationCycleCollection: IHabilitationCycle[] = [
          {
            ...habilitationCycle,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addHabilitationCycleToCollectionIfMissing(habilitationCycleCollection, habilitationCycle);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a HabilitationCycle to an array that doesn't contain it", () => {
        const habilitationCycle: IHabilitationCycle = sampleWithRequiredData;
        const habilitationCycleCollection: IHabilitationCycle[] = [sampleWithPartialData];
        expectedResult = service.addHabilitationCycleToCollectionIfMissing(habilitationCycleCollection, habilitationCycle);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(habilitationCycle);
      });

      it('should add only unique HabilitationCycle to an array', () => {
        const habilitationCycleArray: IHabilitationCycle[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const habilitationCycleCollection: IHabilitationCycle[] = [sampleWithRequiredData];
        expectedResult = service.addHabilitationCycleToCollectionIfMissing(habilitationCycleCollection, ...habilitationCycleArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const habilitationCycle: IHabilitationCycle = sampleWithRequiredData;
        const habilitationCycle2: IHabilitationCycle = sampleWithPartialData;
        expectedResult = service.addHabilitationCycleToCollectionIfMissing([], habilitationCycle, habilitationCycle2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(habilitationCycle);
        expect(expectedResult).toContain(habilitationCycle2);
      });

      it('should accept null and undefined values', () => {
        const habilitationCycle: IHabilitationCycle = sampleWithRequiredData;
        expectedResult = service.addHabilitationCycleToCollectionIfMissing([], null, habilitationCycle, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(habilitationCycle);
      });

      it('should return initial array if no HabilitationCycle is added', () => {
        const habilitationCycleCollection: IHabilitationCycle[] = [sampleWithRequiredData];
        expectedResult = service.addHabilitationCycleToCollectionIfMissing(habilitationCycleCollection, undefined, null);
        expect(expectedResult).toEqual(habilitationCycleCollection);
      });
    });

    describe('compareHabilitationCycle', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareHabilitationCycle(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 3796 };
        const entity2 = null;

        const compareResult1 = service.compareHabilitationCycle(entity1, entity2);
        const compareResult2 = service.compareHabilitationCycle(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 3796 };
        const entity2 = { id: 4619 };

        const compareResult1 = service.compareHabilitationCycle(entity1, entity2);
        const compareResult2 = service.compareHabilitationCycle(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 3796 };
        const entity2 = { id: 3796 };

        const compareResult1 = service.compareHabilitationCycle(entity1, entity2);
        const compareResult2 = service.compareHabilitationCycle(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
