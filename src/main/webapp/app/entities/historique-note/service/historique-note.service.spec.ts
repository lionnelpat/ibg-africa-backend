import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IHistoriqueNote } from '../historique-note.model';
import { sampleWithFullData, sampleWithPartialData, sampleWithRequiredData } from '../historique-note.test-samples';

import { HistoriqueNoteService, RestHistoriqueNote } from './historique-note.service';

const requireRestSample: RestHistoriqueNote = {
  ...sampleWithRequiredData,
  modifieLe: sampleWithRequiredData.modifieLe?.toJSON(),
};

describe('HistoriqueNote Service', () => {
  let service: HistoriqueNoteService;
  let httpMock: HttpTestingController;
  let expectedResult: IHistoriqueNote | IHistoriqueNote[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(HistoriqueNoteService);
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

    it('should return a list of HistoriqueNote', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    describe('addHistoriqueNoteToCollectionIfMissing', () => {
      it('should add a HistoriqueNote to an empty array', () => {
        const historiqueNote: IHistoriqueNote = sampleWithRequiredData;
        expectedResult = service.addHistoriqueNoteToCollectionIfMissing([], historiqueNote);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(historiqueNote);
      });

      it('should not add a HistoriqueNote to an array that contains it', () => {
        const historiqueNote: IHistoriqueNote = sampleWithRequiredData;
        const historiqueNoteCollection: IHistoriqueNote[] = [
          {
            ...historiqueNote,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addHistoriqueNoteToCollectionIfMissing(historiqueNoteCollection, historiqueNote);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a HistoriqueNote to an array that doesn't contain it", () => {
        const historiqueNote: IHistoriqueNote = sampleWithRequiredData;
        const historiqueNoteCollection: IHistoriqueNote[] = [sampleWithPartialData];
        expectedResult = service.addHistoriqueNoteToCollectionIfMissing(historiqueNoteCollection, historiqueNote);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(historiqueNote);
      });

      it('should add only unique HistoriqueNote to an array', () => {
        const historiqueNoteArray: IHistoriqueNote[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const historiqueNoteCollection: IHistoriqueNote[] = [sampleWithRequiredData];
        expectedResult = service.addHistoriqueNoteToCollectionIfMissing(historiqueNoteCollection, ...historiqueNoteArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const historiqueNote: IHistoriqueNote = sampleWithRequiredData;
        const historiqueNote2: IHistoriqueNote = sampleWithPartialData;
        expectedResult = service.addHistoriqueNoteToCollectionIfMissing([], historiqueNote, historiqueNote2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(historiqueNote);
        expect(expectedResult).toContain(historiqueNote2);
      });

      it('should accept null and undefined values', () => {
        const historiqueNote: IHistoriqueNote = sampleWithRequiredData;
        expectedResult = service.addHistoriqueNoteToCollectionIfMissing([], null, historiqueNote, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(historiqueNote);
      });

      it('should return initial array if no HistoriqueNote is added', () => {
        const historiqueNoteCollection: IHistoriqueNote[] = [sampleWithRequiredData];
        expectedResult = service.addHistoriqueNoteToCollectionIfMissing(historiqueNoteCollection, undefined, null);
        expect(expectedResult).toEqual(historiqueNoteCollection);
      });
    });

    describe('compareHistoriqueNote', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareHistoriqueNote(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 3252 };
        const entity2 = null;

        const compareResult1 = service.compareHistoriqueNote(entity1, entity2);
        const compareResult2 = service.compareHistoriqueNote(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 3252 };
        const entity2 = { id: 4745 };

        const compareResult1 = service.compareHistoriqueNote(entity1, entity2);
        const compareResult2 = service.compareHistoriqueNote(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 3252 };
        const entity2 = { id: 3252 };

        const compareResult1 = service.compareHistoriqueNote(entity1, entity2);
        const compareResult2 = service.compareHistoriqueNote(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
