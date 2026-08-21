import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../inscription-cycle.test-samples';

import { InscriptionCycleFormService } from './inscription-cycle-form.service';

describe('InscriptionCycle Form Service', () => {
  let service: InscriptionCycleFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InscriptionCycleFormService);
  });

  describe('Service methods', () => {
    describe('createInscriptionCycleFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createInscriptionCycleFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dateInscription: expect.any(Object),
            cycleTermine: expect.any(Object),
            groupe: expect.any(Object),
            commentaire1: expect.any(Object),
            commentaire2: expect.any(Object),
            commentaire3: expect.any(Object),
            commentaire5: expect.any(Object),
            cycle: expect.any(Object),
            etudiant: expect.any(Object),
          }),
        );
      });

      it('passing IInscriptionCycle should create a new form with FormGroup', () => {
        const formGroup = service.createInscriptionCycleFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dateInscription: expect.any(Object),
            cycleTermine: expect.any(Object),
            groupe: expect.any(Object),
            commentaire1: expect.any(Object),
            commentaire2: expect.any(Object),
            commentaire3: expect.any(Object),
            commentaire5: expect.any(Object),
            cycle: expect.any(Object),
            etudiant: expect.any(Object),
          }),
        );
      });
    });

    describe('getInscriptionCycle', () => {
      it('should return NewInscriptionCycle for default InscriptionCycle initial value', () => {
        const formGroup = service.createInscriptionCycleFormGroup(sampleWithNewData);

        const inscriptionCycle = service.getInscriptionCycle(formGroup) as any;

        expect(inscriptionCycle).toMatchObject(sampleWithNewData);
      });

      it('should return NewInscriptionCycle for empty InscriptionCycle initial value', () => {
        const formGroup = service.createInscriptionCycleFormGroup();

        const inscriptionCycle = service.getInscriptionCycle(formGroup) as any;

        expect(inscriptionCycle).toMatchObject({});
      });

      it('should return IInscriptionCycle', () => {
        const formGroup = service.createInscriptionCycleFormGroup(sampleWithRequiredData);

        const inscriptionCycle = service.getInscriptionCycle(formGroup) as any;

        expect(inscriptionCycle).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IInscriptionCycle should not enable id FormControl', () => {
        const formGroup = service.createInscriptionCycleFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewInscriptionCycle should disable id FormControl', () => {
        const formGroup = service.createInscriptionCycleFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
