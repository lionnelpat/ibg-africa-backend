import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IEvenementEtudiant } from '../evenement-etudiant.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../evenement-etudiant.test-samples';

import { EvenementEtudiantService, RestEvenementEtudiant } from './evenement-etudiant.service';

const requireRestSample: RestEvenementEtudiant = {
  ...sampleWithRequiredData,
  dateEvenement: sampleWithRequiredData.dateEvenement?.format(DATE_FORMAT),
};

describe('EvenementEtudiant Service', () => {
  let service: EvenementEtudiantService;
  let httpMock: HttpTestingController;
  let expectedResult: IEvenementEtudiant | IEvenementEtudiant[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(EvenementEtudiantService);
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

    it('should create a EvenementEtudiant', () => {
      const evenementEtudiant = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(evenementEtudiant).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a EvenementEtudiant', () => {
      const evenementEtudiant = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(evenementEtudiant).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a EvenementEtudiant', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of EvenementEtudiant', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a EvenementEtudiant', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addEvenementEtudiantToCollectionIfMissing', () => {
      it('should add a EvenementEtudiant to an empty array', () => {
        const evenementEtudiant: IEvenementEtudiant = sampleWithRequiredData;
        expectedResult = service.addEvenementEtudiantToCollectionIfMissing([], evenementEtudiant);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(evenementEtudiant);
      });

      it('should not add a EvenementEtudiant to an array that contains it', () => {
        const evenementEtudiant: IEvenementEtudiant = sampleWithRequiredData;
        const evenementEtudiantCollection: IEvenementEtudiant[] = [
          {
            ...evenementEtudiant,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addEvenementEtudiantToCollectionIfMissing(evenementEtudiantCollection, evenementEtudiant);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a EvenementEtudiant to an array that doesn't contain it", () => {
        const evenementEtudiant: IEvenementEtudiant = sampleWithRequiredData;
        const evenementEtudiantCollection: IEvenementEtudiant[] = [sampleWithPartialData];
        expectedResult = service.addEvenementEtudiantToCollectionIfMissing(evenementEtudiantCollection, evenementEtudiant);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(evenementEtudiant);
      });

      it('should add only unique EvenementEtudiant to an array', () => {
        const evenementEtudiantArray: IEvenementEtudiant[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const evenementEtudiantCollection: IEvenementEtudiant[] = [sampleWithRequiredData];
        expectedResult = service.addEvenementEtudiantToCollectionIfMissing(evenementEtudiantCollection, ...evenementEtudiantArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const evenementEtudiant: IEvenementEtudiant = sampleWithRequiredData;
        const evenementEtudiant2: IEvenementEtudiant = sampleWithPartialData;
        expectedResult = service.addEvenementEtudiantToCollectionIfMissing([], evenementEtudiant, evenementEtudiant2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(evenementEtudiant);
        expect(expectedResult).toContain(evenementEtudiant2);
      });

      it('should accept null and undefined values', () => {
        const evenementEtudiant: IEvenementEtudiant = sampleWithRequiredData;
        expectedResult = service.addEvenementEtudiantToCollectionIfMissing([], null, evenementEtudiant, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(evenementEtudiant);
      });

      it('should return initial array if no EvenementEtudiant is added', () => {
        const evenementEtudiantCollection: IEvenementEtudiant[] = [sampleWithRequiredData];
        expectedResult = service.addEvenementEtudiantToCollectionIfMissing(evenementEtudiantCollection, undefined, null);
        expect(expectedResult).toEqual(evenementEtudiantCollection);
      });
    });

    describe('compareEvenementEtudiant', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareEvenementEtudiant(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 2800 };
        const entity2 = null;

        const compareResult1 = service.compareEvenementEtudiant(entity1, entity2);
        const compareResult2 = service.compareEvenementEtudiant(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 2800 };
        const entity2 = { id: 116 };

        const compareResult1 = service.compareEvenementEtudiant(entity1, entity2);
        const compareResult2 = service.compareEvenementEtudiant(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 2800 };
        const entity2 = { id: 2800 };

        const compareResult1 = service.compareEvenementEtudiant(entity1, entity2);
        const compareResult2 = service.compareEvenementEtudiant(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
