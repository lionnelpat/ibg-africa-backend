import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../evenement-etudiant.test-samples';

import { EvenementEtudiantFormService } from './evenement-etudiant-form.service';

describe('EvenementEtudiant Form Service', () => {
  let service: EvenementEtudiantFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EvenementEtudiantFormService);
  });

  describe('Service methods', () => {
    describe('createEvenementEtudiantFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEvenementEtudiantFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dateEvenement: expect.any(Object),
            intitule: expect.any(Object),
            commentaire: expect.any(Object),
            etudiant: expect.any(Object),
          }),
        );
      });

      it('passing IEvenementEtudiant should create a new form with FormGroup', () => {
        const formGroup = service.createEvenementEtudiantFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            dateEvenement: expect.any(Object),
            intitule: expect.any(Object),
            commentaire: expect.any(Object),
            etudiant: expect.any(Object),
          }),
        );
      });
    });

    describe('getEvenementEtudiant', () => {
      it('should return NewEvenementEtudiant for default EvenementEtudiant initial value', () => {
        const formGroup = service.createEvenementEtudiantFormGroup(sampleWithNewData);

        const evenementEtudiant = service.getEvenementEtudiant(formGroup) as any;

        expect(evenementEtudiant).toMatchObject(sampleWithNewData);
      });

      it('should return NewEvenementEtudiant for empty EvenementEtudiant initial value', () => {
        const formGroup = service.createEvenementEtudiantFormGroup();

        const evenementEtudiant = service.getEvenementEtudiant(formGroup) as any;

        expect(evenementEtudiant).toMatchObject({});
      });

      it('should return IEvenementEtudiant', () => {
        const formGroup = service.createEvenementEtudiantFormGroup(sampleWithRequiredData);

        const evenementEtudiant = service.getEvenementEtudiant(formGroup) as any;

        expect(evenementEtudiant).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEvenementEtudiant should not enable id FormControl', () => {
        const formGroup = service.createEvenementEtudiantFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEvenementEtudiant should disable id FormControl', () => {
        const formGroup = service.createEvenementEtudiantFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
