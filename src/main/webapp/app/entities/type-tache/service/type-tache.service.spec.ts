import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ITypeTache } from '../type-tache.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../type-tache.test-samples';

import { TypeTacheService } from './type-tache.service';

const requireRestSample: ITypeTache = {
  ...sampleWithRequiredData,
};

describe('TypeTache Service', () => {
  let service: TypeTacheService;
  let httpMock: HttpTestingController;
  let expectedResult: ITypeTache | ITypeTache[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(TypeTacheService);
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

    it('should create a TypeTache', () => {
      const typeTache = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(typeTache).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TypeTache', () => {
      const typeTache = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(typeTache).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TypeTache', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TypeTache', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TypeTache', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTypeTacheToCollectionIfMissing', () => {
      it('should add a TypeTache to an empty array', () => {
        const typeTache: ITypeTache = sampleWithRequiredData;
        expectedResult = service.addTypeTacheToCollectionIfMissing([], typeTache);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(typeTache);
      });

      it('should not add a TypeTache to an array that contains it', () => {
        const typeTache: ITypeTache = sampleWithRequiredData;
        const typeTacheCollection: ITypeTache[] = [
          {
            ...typeTache,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTypeTacheToCollectionIfMissing(typeTacheCollection, typeTache);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TypeTache to an array that doesn't contain it", () => {
        const typeTache: ITypeTache = sampleWithRequiredData;
        const typeTacheCollection: ITypeTache[] = [sampleWithPartialData];
        expectedResult = service.addTypeTacheToCollectionIfMissing(typeTacheCollection, typeTache);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(typeTache);
      });

      it('should add only unique TypeTache to an array', () => {
        const typeTacheArray: ITypeTache[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const typeTacheCollection: ITypeTache[] = [sampleWithRequiredData];
        expectedResult = service.addTypeTacheToCollectionIfMissing(typeTacheCollection, ...typeTacheArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const typeTache: ITypeTache = sampleWithRequiredData;
        const typeTache2: ITypeTache = sampleWithPartialData;
        expectedResult = service.addTypeTacheToCollectionIfMissing([], typeTache, typeTache2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(typeTache);
        expect(expectedResult).toContain(typeTache2);
      });

      it('should accept null and undefined values', () => {
        const typeTache: ITypeTache = sampleWithRequiredData;
        expectedResult = service.addTypeTacheToCollectionIfMissing([], null, typeTache, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(typeTache);
      });

      it('should return initial array if no TypeTache is added', () => {
        const typeTacheCollection: ITypeTache[] = [sampleWithRequiredData];
        expectedResult = service.addTypeTacheToCollectionIfMissing(typeTacheCollection, undefined, null);
        expect(expectedResult).toEqual(typeTacheCollection);
      });
    });

    describe('compareTypeTache', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTypeTache(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 8191 };
        const entity2 = null;

        const compareResult1 = service.compareTypeTache(entity1, entity2);
        const compareResult2 = service.compareTypeTache(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 8191 };
        const entity2 = { id: 2439 };

        const compareResult1 = service.compareTypeTache(entity1, entity2);
        const compareResult2 = service.compareTypeTache(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 8191 };
        const entity2 = { id: 8191 };

        const compareResult1 = service.compareTypeTache(entity1, entity2);
        const compareResult2 = service.compareTypeTache(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
