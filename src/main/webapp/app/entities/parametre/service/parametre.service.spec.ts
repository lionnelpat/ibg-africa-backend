import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IParametre } from '../parametre.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../parametre.test-samples';

import { ParametreService } from './parametre.service';

const requireRestSample: IParametre = {
  ...sampleWithRequiredData,
};

describe('Parametre Service', () => {
  let service: ParametreService;
  let httpMock: HttpTestingController;
  let expectedResult: IParametre | IParametre[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ParametreService);
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

    it('should create a Parametre', () => {
      const parametre = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(parametre).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Parametre', () => {
      const parametre = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(parametre).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Parametre', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Parametre', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Parametre', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addParametreToCollectionIfMissing', () => {
      it('should add a Parametre to an empty array', () => {
        const parametre: IParametre = sampleWithRequiredData;
        expectedResult = service.addParametreToCollectionIfMissing([], parametre);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(parametre);
      });

      it('should not add a Parametre to an array that contains it', () => {
        const parametre: IParametre = sampleWithRequiredData;
        const parametreCollection: IParametre[] = [
          {
            ...parametre,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addParametreToCollectionIfMissing(parametreCollection, parametre);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Parametre to an array that doesn't contain it", () => {
        const parametre: IParametre = sampleWithRequiredData;
        const parametreCollection: IParametre[] = [sampleWithPartialData];
        expectedResult = service.addParametreToCollectionIfMissing(parametreCollection, parametre);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(parametre);
      });

      it('should add only unique Parametre to an array', () => {
        const parametreArray: IParametre[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const parametreCollection: IParametre[] = [sampleWithRequiredData];
        expectedResult = service.addParametreToCollectionIfMissing(parametreCollection, ...parametreArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const parametre: IParametre = sampleWithRequiredData;
        const parametre2: IParametre = sampleWithPartialData;
        expectedResult = service.addParametreToCollectionIfMissing([], parametre, parametre2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(parametre);
        expect(expectedResult).toContain(parametre2);
      });

      it('should accept null and undefined values', () => {
        const parametre: IParametre = sampleWithRequiredData;
        expectedResult = service.addParametreToCollectionIfMissing([], null, parametre, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(parametre);
      });

      it('should return initial array if no Parametre is added', () => {
        const parametreCollection: IParametre[] = [sampleWithRequiredData];
        expectedResult = service.addParametreToCollectionIfMissing(parametreCollection, undefined, null);
        expect(expectedResult).toEqual(parametreCollection);
      });
    });

    describe('compareParametre', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareParametre(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 6734 };
        const entity2 = null;

        const compareResult1 = service.compareParametre(entity1, entity2);
        const compareResult2 = service.compareParametre(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 6734 };
        const entity2 = { id: 17145 };

        const compareResult1 = service.compareParametre(entity1, entity2);
        const compareResult2 = service.compareParametre(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 6734 };
        const entity2 = { id: 6734 };

        const compareResult1 = service.compareParametre(entity1, entity2);
        const compareResult2 = service.compareParametre(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
