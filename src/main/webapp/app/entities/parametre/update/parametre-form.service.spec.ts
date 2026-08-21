import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../parametre.test-samples';

import { ParametreFormService } from './parametre-form.service';

describe('Parametre Form Service', () => {
  let service: ParametreFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ParametreFormService);
  });

  describe('Service methods', () => {
    describe('createParametreFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createParametreFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            cle: expect.any(Object),
            libelle: expect.any(Object),
            valeur: expect.any(Object),
            typeValeur: expect.any(Object),
            modifiableUi: expect.any(Object),
            centre: expect.any(Object),
          }),
        );
      });

      it('passing IParametre should create a new form with FormGroup', () => {
        const formGroup = service.createParametreFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            cle: expect.any(Object),
            libelle: expect.any(Object),
            valeur: expect.any(Object),
            typeValeur: expect.any(Object),
            modifiableUi: expect.any(Object),
            centre: expect.any(Object),
          }),
        );
      });
    });

    describe('getParametre', () => {
      it('should return NewParametre for default Parametre initial value', () => {
        const formGroup = service.createParametreFormGroup(sampleWithNewData);

        const parametre = service.getParametre(formGroup) as any;

        expect(parametre).toMatchObject(sampleWithNewData);
      });

      it('should return NewParametre for empty Parametre initial value', () => {
        const formGroup = service.createParametreFormGroup();

        const parametre = service.getParametre(formGroup) as any;

        expect(parametre).toMatchObject({});
      });

      it('should return IParametre', () => {
        const formGroup = service.createParametreFormGroup(sampleWithRequiredData);

        const parametre = service.getParametre(formGroup) as any;

        expect(parametre).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IParametre should not enable id FormControl', () => {
        const formGroup = service.createParametreFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewParametre should disable id FormControl', () => {
        const formGroup = service.createParametreFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
