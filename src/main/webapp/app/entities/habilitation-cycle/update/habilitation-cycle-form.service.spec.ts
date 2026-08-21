import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../habilitation-cycle.test-samples';

import { HabilitationCycleFormService } from './habilitation-cycle-form.service';

describe('HabilitationCycle Form Service', () => {
  let service: HabilitationCycleFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HabilitationCycleFormService);
  });

  describe('Service methods', () => {
    describe('createHabilitationCycleFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createHabilitationCycleFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            keycloakUserId: expect.any(Object),
            roleFonctionnel: expect.any(Object),
            dateDebut: expect.any(Object),
            dateFin: expect.any(Object),
            centre: expect.any(Object),
            cycle: expect.any(Object),
          }),
        );
      });

      it('passing IHabilitationCycle should create a new form with FormGroup', () => {
        const formGroup = service.createHabilitationCycleFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            keycloakUserId: expect.any(Object),
            roleFonctionnel: expect.any(Object),
            dateDebut: expect.any(Object),
            dateFin: expect.any(Object),
            centre: expect.any(Object),
            cycle: expect.any(Object),
          }),
        );
      });
    });

    describe('getHabilitationCycle', () => {
      it('should return NewHabilitationCycle for default HabilitationCycle initial value', () => {
        const formGroup = service.createHabilitationCycleFormGroup(sampleWithNewData);

        const habilitationCycle = service.getHabilitationCycle(formGroup) as any;

        expect(habilitationCycle).toMatchObject(sampleWithNewData);
      });

      it('should return NewHabilitationCycle for empty HabilitationCycle initial value', () => {
        const formGroup = service.createHabilitationCycleFormGroup();

        const habilitationCycle = service.getHabilitationCycle(formGroup) as any;

        expect(habilitationCycle).toMatchObject({});
      });

      it('should return IHabilitationCycle', () => {
        const formGroup = service.createHabilitationCycleFormGroup(sampleWithRequiredData);

        const habilitationCycle = service.getHabilitationCycle(formGroup) as any;

        expect(habilitationCycle).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IHabilitationCycle should not enable id FormControl', () => {
        const formGroup = service.createHabilitationCycleFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewHabilitationCycle should disable id FormControl', () => {
        const formGroup = service.createHabilitationCycleFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
