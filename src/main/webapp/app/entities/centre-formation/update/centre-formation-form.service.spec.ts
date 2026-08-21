import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../centre-formation.test-samples';

import { CentreFormationFormService } from './centre-formation-form.service';

describe('CentreFormation Form Service', () => {
  let service: CentreFormationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CentreFormationFormService);
  });

  describe('Service methods', () => {
    describe('createCentreFormationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCentreFormationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            nom: expect.any(Object),
            ville: expect.any(Object),
            adresse: expect.any(Object),
            enteteDocument: expect.any(Object),
            signataire: expect.any(Object),
            logoUrl: expect.any(Object),
            nbCyclesCursus: expect.any(Object),
            noteMaximale: expect.any(Object),
            actif: expect.any(Object),
            pays: expect.any(Object),
          }),
        );
      });

      it('passing ICentreFormation should create a new form with FormGroup', () => {
        const formGroup = service.createCentreFormationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            nom: expect.any(Object),
            ville: expect.any(Object),
            adresse: expect.any(Object),
            enteteDocument: expect.any(Object),
            signataire: expect.any(Object),
            logoUrl: expect.any(Object),
            nbCyclesCursus: expect.any(Object),
            noteMaximale: expect.any(Object),
            actif: expect.any(Object),
            pays: expect.any(Object),
          }),
        );
      });
    });

    describe('getCentreFormation', () => {
      it('should return NewCentreFormation for default CentreFormation initial value', () => {
        const formGroup = service.createCentreFormationFormGroup(sampleWithNewData);

        const centreFormation = service.getCentreFormation(formGroup) as any;

        expect(centreFormation).toMatchObject(sampleWithNewData);
      });

      it('should return NewCentreFormation for empty CentreFormation initial value', () => {
        const formGroup = service.createCentreFormationFormGroup();

        const centreFormation = service.getCentreFormation(formGroup) as any;

        expect(centreFormation).toMatchObject({});
      });

      it('should return ICentreFormation', () => {
        const formGroup = service.createCentreFormationFormGroup(sampleWithRequiredData);

        const centreFormation = service.getCentreFormation(formGroup) as any;

        expect(centreFormation).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICentreFormation should not enable id FormControl', () => {
        const formGroup = service.createCentreFormationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCentreFormation should disable id FormControl', () => {
        const formGroup = service.createCentreFormationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
