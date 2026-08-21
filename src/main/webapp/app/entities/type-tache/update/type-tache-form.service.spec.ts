import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../type-tache.test-samples';

import { TypeTacheFormService } from './type-tache-form.service';

describe('TypeTache Form Service', () => {
  let service: TypeTacheFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TypeTacheFormService);
  });

  describe('Service methods', () => {
    describe('createTypeTacheFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTypeTacheFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            intitule: expect.any(Object),
            libelleLong: expect.any(Object),
            libelleCourt: expect.any(Object),
            entreDansMoyenne: expect.any(Object),
            commentaire: expect.any(Object),
            actif: expect.any(Object),
          }),
        );
      });

      it('passing ITypeTache should create a new form with FormGroup', () => {
        const formGroup = service.createTypeTacheFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            intitule: expect.any(Object),
            libelleLong: expect.any(Object),
            libelleCourt: expect.any(Object),
            entreDansMoyenne: expect.any(Object),
            commentaire: expect.any(Object),
            actif: expect.any(Object),
          }),
        );
      });
    });

    describe('getTypeTache', () => {
      it('should return NewTypeTache for default TypeTache initial value', () => {
        const formGroup = service.createTypeTacheFormGroup(sampleWithNewData);

        const typeTache = service.getTypeTache(formGroup) as any;

        expect(typeTache).toMatchObject(sampleWithNewData);
      });

      it('should return NewTypeTache for empty TypeTache initial value', () => {
        const formGroup = service.createTypeTacheFormGroup();

        const typeTache = service.getTypeTache(formGroup) as any;

        expect(typeTache).toMatchObject({});
      });

      it('should return ITypeTache', () => {
        const formGroup = service.createTypeTacheFormGroup(sampleWithRequiredData);

        const typeTache = service.getTypeTache(formGroup) as any;

        expect(typeTache).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITypeTache should not enable id FormControl', () => {
        const formGroup = service.createTypeTacheFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTypeTache should disable id FormControl', () => {
        const formGroup = service.createTypeTacheFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
