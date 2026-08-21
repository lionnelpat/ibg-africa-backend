import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../evaluation-realisee.test-samples';

import { EvaluationRealiseeFormService } from './evaluation-realisee-form.service';

describe('EvaluationRealisee Form Service', () => {
  let service: EvaluationRealiseeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EvaluationRealiseeFormService);
  });

  describe('Service methods', () => {
    describe('createEvaluationRealiseeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEvaluationRealiseeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            note: expect.any(Object),
            statut: expect.any(Object),
            compteDansMoyenne: expect.any(Object),
            dateDebut: expect.any(Object),
            dateFin: expect.any(Object),
            commentaire1: expect.any(Object),
            commentaire2: expect.any(Object),
            commentaire3: expect.any(Object),
            saisiePar: expect.any(Object),
            saisieLe: expect.any(Object),
            valideePar: expect.any(Object),
            valideeLe: expect.any(Object),
            evaluationPrevue: expect.any(Object),
            etudiant: expect.any(Object),
          }),
        );
      });

      it('passing IEvaluationRealisee should create a new form with FormGroup', () => {
        const formGroup = service.createEvaluationRealiseeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            note: expect.any(Object),
            statut: expect.any(Object),
            compteDansMoyenne: expect.any(Object),
            dateDebut: expect.any(Object),
            dateFin: expect.any(Object),
            commentaire1: expect.any(Object),
            commentaire2: expect.any(Object),
            commentaire3: expect.any(Object),
            saisiePar: expect.any(Object),
            saisieLe: expect.any(Object),
            valideePar: expect.any(Object),
            valideeLe: expect.any(Object),
            evaluationPrevue: expect.any(Object),
            etudiant: expect.any(Object),
          }),
        );
      });
    });

    describe('getEvaluationRealisee', () => {
      it('should return NewEvaluationRealisee for default EvaluationRealisee initial value', () => {
        const formGroup = service.createEvaluationRealiseeFormGroup(sampleWithNewData);

        const evaluationRealisee = service.getEvaluationRealisee(formGroup) as any;

        expect(evaluationRealisee).toMatchObject(sampleWithNewData);
      });

      it('should return NewEvaluationRealisee for empty EvaluationRealisee initial value', () => {
        const formGroup = service.createEvaluationRealiseeFormGroup();

        const evaluationRealisee = service.getEvaluationRealisee(formGroup) as any;

        expect(evaluationRealisee).toMatchObject({});
      });

      it('should return IEvaluationRealisee', () => {
        const formGroup = service.createEvaluationRealiseeFormGroup(sampleWithRequiredData);

        const evaluationRealisee = service.getEvaluationRealisee(formGroup) as any;

        expect(evaluationRealisee).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEvaluationRealisee should not enable id FormControl', () => {
        const formGroup = service.createEvaluationRealiseeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEvaluationRealisee should disable id FormControl', () => {
        const formGroup = service.createEvaluationRealiseeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
