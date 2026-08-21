import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IBaremeMention } from '../bareme-mention.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../bareme-mention.test-samples';

import { BaremeMentionService } from './bareme-mention.service';

const requireRestSample: IBaremeMention = {
  ...sampleWithRequiredData,
};

describe('BaremeMention Service', () => {
  let service: BaremeMentionService;
  let httpMock: HttpTestingController;
  let expectedResult: IBaremeMention | IBaremeMention[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(BaremeMentionService);
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

    it('should create a BaremeMention', () => {
      const baremeMention = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(baremeMention).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a BaremeMention', () => {
      const baremeMention = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(baremeMention).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a BaremeMention', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of BaremeMention', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a BaremeMention', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addBaremeMentionToCollectionIfMissing', () => {
      it('should add a BaremeMention to an empty array', () => {
        const baremeMention: IBaremeMention = sampleWithRequiredData;
        expectedResult = service.addBaremeMentionToCollectionIfMissing([], baremeMention);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(baremeMention);
      });

      it('should not add a BaremeMention to an array that contains it', () => {
        const baremeMention: IBaremeMention = sampleWithRequiredData;
        const baremeMentionCollection: IBaremeMention[] = [
          {
            ...baremeMention,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addBaremeMentionToCollectionIfMissing(baremeMentionCollection, baremeMention);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a BaremeMention to an array that doesn't contain it", () => {
        const baremeMention: IBaremeMention = sampleWithRequiredData;
        const baremeMentionCollection: IBaremeMention[] = [sampleWithPartialData];
        expectedResult = service.addBaremeMentionToCollectionIfMissing(baremeMentionCollection, baremeMention);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(baremeMention);
      });

      it('should add only unique BaremeMention to an array', () => {
        const baremeMentionArray: IBaremeMention[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const baremeMentionCollection: IBaremeMention[] = [sampleWithRequiredData];
        expectedResult = service.addBaremeMentionToCollectionIfMissing(baremeMentionCollection, ...baremeMentionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const baremeMention: IBaremeMention = sampleWithRequiredData;
        const baremeMention2: IBaremeMention = sampleWithPartialData;
        expectedResult = service.addBaremeMentionToCollectionIfMissing([], baremeMention, baremeMention2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(baremeMention);
        expect(expectedResult).toContain(baremeMention2);
      });

      it('should accept null and undefined values', () => {
        const baremeMention: IBaremeMention = sampleWithRequiredData;
        expectedResult = service.addBaremeMentionToCollectionIfMissing([], null, baremeMention, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(baremeMention);
      });

      it('should return initial array if no BaremeMention is added', () => {
        const baremeMentionCollection: IBaremeMention[] = [sampleWithRequiredData];
        expectedResult = service.addBaremeMentionToCollectionIfMissing(baremeMentionCollection, undefined, null);
        expect(expectedResult).toEqual(baremeMentionCollection);
      });
    });

    describe('compareBaremeMention', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareBaremeMention(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 25091 };
        const entity2 = null;

        const compareResult1 = service.compareBaremeMention(entity1, entity2);
        const compareResult2 = service.compareBaremeMention(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 25091 };
        const entity2 = { id: 32244 };

        const compareResult1 = service.compareBaremeMention(entity1, entity2);
        const compareResult2 = service.compareBaremeMention(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 25091 };
        const entity2 = { id: 25091 };

        const compareResult1 = service.compareBaremeMention(entity1, entity2);
        const compareResult2 = service.compareBaremeMention(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
