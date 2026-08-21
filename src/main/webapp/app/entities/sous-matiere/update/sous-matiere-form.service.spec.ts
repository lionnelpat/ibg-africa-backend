import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../sous-matiere.test-samples';

import { SousMatiereFormService } from './sous-matiere-form.service';

describe('SousMatiere Form Service', () => {
  let service: SousMatiereFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SousMatiereFormService);
  });

  describe('Service methods', () => {
    describe('createSousMatiereFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSousMatiereFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            intitule: expect.any(Object),
            libelleLong: expect.any(Object),
            libelleCourt: expect.any(Object),
            commentaire: expect.any(Object),
            actif: expect.any(Object),
          }),
        );
      });

      it('passing ISousMatiere should create a new form with FormGroup', () => {
        const formGroup = service.createSousMatiereFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            intitule: expect.any(Object),
            libelleLong: expect.any(Object),
            libelleCourt: expect.any(Object),
            commentaire: expect.any(Object),
            actif: expect.any(Object),
          }),
        );
      });
    });

    describe('getSousMatiere', () => {
      it('should return NewSousMatiere for default SousMatiere initial value', () => {
        const formGroup = service.createSousMatiereFormGroup(sampleWithNewData);

        const sousMatiere = service.getSousMatiere(formGroup) as any;

        expect(sousMatiere).toMatchObject(sampleWithNewData);
      });

      it('should return NewSousMatiere for empty SousMatiere initial value', () => {
        const formGroup = service.createSousMatiereFormGroup();

        const sousMatiere = service.getSousMatiere(formGroup) as any;

        expect(sousMatiere).toMatchObject({});
      });

      it('should return ISousMatiere', () => {
        const formGroup = service.createSousMatiereFormGroup(sampleWithRequiredData);

        const sousMatiere = service.getSousMatiere(formGroup) as any;

        expect(sousMatiere).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISousMatiere should not enable id FormControl', () => {
        const formGroup = service.createSousMatiereFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSousMatiere should disable id FormControl', () => {
        const formGroup = service.createSousMatiereFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
