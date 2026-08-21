import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../evaluation-prevue.test-samples';

import { EvaluationPrevueFormService } from './evaluation-prevue-form.service';

describe('EvaluationPrevue Form Service', () => {
  let service: EvaluationPrevueFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EvaluationPrevueFormService);
  });

  describe('Service methods', () => {
    describe('createEvaluationPrevueFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEvaluationPrevueFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            intitule: expect.any(Object),
            libelleImpression: expect.any(Object),
            coefficient: expect.any(Object),
            compteDansMoyenne: expect.any(Object),
            noteMaximale: expect.any(Object),
            dateDebut: expect.any(Object),
            dateFin: expect.any(Object),
            commentaire: expect.any(Object),
            cycle: expect.any(Object),
            enseignant: expect.any(Object),
            matiere: expect.any(Object),
            sousMatiere: expect.any(Object),
            cours: expect.any(Object),
            typeTache: expect.any(Object),
          }),
        );
      });

      it('passing IEvaluationPrevue should create a new form with FormGroup', () => {
        const formGroup = service.createEvaluationPrevueFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            intitule: expect.any(Object),
            libelleImpression: expect.any(Object),
            coefficient: expect.any(Object),
            compteDansMoyenne: expect.any(Object),
            noteMaximale: expect.any(Object),
            dateDebut: expect.any(Object),
            dateFin: expect.any(Object),
            commentaire: expect.any(Object),
            cycle: expect.any(Object),
            enseignant: expect.any(Object),
            matiere: expect.any(Object),
            sousMatiere: expect.any(Object),
            cours: expect.any(Object),
            typeTache: expect.any(Object),
          }),
        );
      });
    });

    describe('getEvaluationPrevue', () => {
      it('should return NewEvaluationPrevue for default EvaluationPrevue initial value', () => {
        const formGroup = service.createEvaluationPrevueFormGroup(sampleWithNewData);

        const evaluationPrevue = service.getEvaluationPrevue(formGroup) as any;

        expect(evaluationPrevue).toMatchObject(sampleWithNewData);
      });

      it('should return NewEvaluationPrevue for empty EvaluationPrevue initial value', () => {
        const formGroup = service.createEvaluationPrevueFormGroup();

        const evaluationPrevue = service.getEvaluationPrevue(formGroup) as any;

        expect(evaluationPrevue).toMatchObject({});
      });

      it('should return IEvaluationPrevue', () => {
        const formGroup = service.createEvaluationPrevueFormGroup(sampleWithRequiredData);

        const evaluationPrevue = service.getEvaluationPrevue(formGroup) as any;

        expect(evaluationPrevue).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEvaluationPrevue should not enable id FormControl', () => {
        const formGroup = service.createEvaluationPrevueFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEvaluationPrevue should disable id FormControl', () => {
        const formGroup = service.createEvaluationPrevueFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
