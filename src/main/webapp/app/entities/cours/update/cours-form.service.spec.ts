import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../cours.test-samples';

import { CoursFormService } from './cours-form.service';

describe('Cours Form Service', () => {
  let service: CoursFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CoursFormService);
  });

  describe('Service methods', () => {
    describe('createCoursFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCoursFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            intitule: expect.any(Object),
            libelleLong: expect.any(Object),
            libelleCourt: expect.any(Object),
            ordreAffichage: expect.any(Object),
            nbPeriodes: expect.any(Object),
            coefficient: expect.any(Object),
            dateDebut: expect.any(Object),
            dateFin: expect.any(Object),
            commentaire: expect.any(Object),
            actif: expect.any(Object),
          }),
        );
      });

      it('passing ICours should create a new form with FormGroup', () => {
        const formGroup = service.createCoursFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            intitule: expect.any(Object),
            libelleLong: expect.any(Object),
            libelleCourt: expect.any(Object),
            ordreAffichage: expect.any(Object),
            nbPeriodes: expect.any(Object),
            coefficient: expect.any(Object),
            dateDebut: expect.any(Object),
            dateFin: expect.any(Object),
            commentaire: expect.any(Object),
            actif: expect.any(Object),
          }),
        );
      });
    });

    describe('getCours', () => {
      it('should return NewCours for default Cours initial value', () => {
        const formGroup = service.createCoursFormGroup(sampleWithNewData);

        const cours = service.getCours(formGroup) as any;

        expect(cours).toMatchObject(sampleWithNewData);
      });

      it('should return NewCours for empty Cours initial value', () => {
        const formGroup = service.createCoursFormGroup();

        const cours = service.getCours(formGroup) as any;

        expect(cours).toMatchObject({});
      });

      it('should return ICours', () => {
        const formGroup = service.createCoursFormGroup(sampleWithRequiredData);

        const cours = service.getCours(formGroup) as any;

        expect(cours).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICours should not enable id FormControl', () => {
        const formGroup = service.createCoursFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCours should disable id FormControl', () => {
        const formGroup = service.createCoursFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
