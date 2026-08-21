import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../bareme-mention.test-samples';

import { BaremeMentionFormService } from './bareme-mention-form.service';

describe('BaremeMention Form Service', () => {
  let service: BaremeMentionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BaremeMentionFormService);
  });

  describe('Service methods', () => {
    describe('createBaremeMentionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBaremeMentionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            libelleLong: expect.any(Object),
            libelleCourt: expect.any(Object),
            borneMin: expect.any(Object),
            minInclus: expect.any(Object),
            borneMax: expect.any(Object),
            maxInclus: expect.any(Object),
            ordreAffichage: expect.any(Object),
            commentaire: expect.any(Object),
            actif: expect.any(Object),
            centre: expect.any(Object),
          }),
        );
      });

      it('passing IBaremeMention should create a new form with FormGroup', () => {
        const formGroup = service.createBaremeMentionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            libelleLong: expect.any(Object),
            libelleCourt: expect.any(Object),
            borneMin: expect.any(Object),
            minInclus: expect.any(Object),
            borneMax: expect.any(Object),
            maxInclus: expect.any(Object),
            ordreAffichage: expect.any(Object),
            commentaire: expect.any(Object),
            actif: expect.any(Object),
            centre: expect.any(Object),
          }),
        );
      });
    });

    describe('getBaremeMention', () => {
      it('should return NewBaremeMention for default BaremeMention initial value', () => {
        const formGroup = service.createBaremeMentionFormGroup(sampleWithNewData);

        const baremeMention = service.getBaremeMention(formGroup) as any;

        expect(baremeMention).toMatchObject(sampleWithNewData);
      });

      it('should return NewBaremeMention for empty BaremeMention initial value', () => {
        const formGroup = service.createBaremeMentionFormGroup();

        const baremeMention = service.getBaremeMention(formGroup) as any;

        expect(baremeMention).toMatchObject({});
      });

      it('should return IBaremeMention', () => {
        const formGroup = service.createBaremeMentionFormGroup(sampleWithRequiredData);

        const baremeMention = service.getBaremeMention(formGroup) as any;

        expect(baremeMention).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBaremeMention should not enable id FormControl', () => {
        const formGroup = service.createBaremeMentionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBaremeMention should disable id FormControl', () => {
        const formGroup = service.createBaremeMentionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
