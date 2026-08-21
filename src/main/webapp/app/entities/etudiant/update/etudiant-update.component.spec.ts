import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IPays } from 'app/entities/pays/pays.model';
import { PaysService } from 'app/entities/pays/service/pays.service';
import { EtudiantService } from '../service/etudiant.service';
import { IEtudiant } from '../etudiant.model';
import { EtudiantFormService } from './etudiant-form.service';

import { EtudiantUpdateComponent } from './etudiant-update.component';

describe('Etudiant Management Update Component', () => {
  let comp: EtudiantUpdateComponent;
  let fixture: ComponentFixture<EtudiantUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let etudiantFormService: EtudiantFormService;
  let etudiantService: EtudiantService;
  let paysService: PaysService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [EtudiantUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(EtudiantUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EtudiantUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    etudiantFormService = TestBed.inject(EtudiantFormService);
    etudiantService = TestBed.inject(EtudiantService);
    paysService = TestBed.inject(PaysService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Pays query and add missing value', () => {
      const etudiant: IEtudiant = { id: 14632 };
      const pays: IPays = { id: 21471 };
      etudiant.pays = pays;

      const paysCollection: IPays[] = [{ id: 21471 }];
      jest.spyOn(paysService, 'query').mockReturnValue(of(new HttpResponse({ body: paysCollection })));
      const additionalPays = [pays];
      const expectedCollection: IPays[] = [...additionalPays, ...paysCollection];
      jest.spyOn(paysService, 'addPaysToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ etudiant });
      comp.ngOnInit();

      expect(paysService.query).toHaveBeenCalled();
      expect(paysService.addPaysToCollectionIfMissing).toHaveBeenCalledWith(paysCollection, ...additionalPays.map(expect.objectContaining));
      expect(comp.paysSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const etudiant: IEtudiant = { id: 14632 };
      const pays: IPays = { id: 21471 };
      etudiant.pays = pays;

      activatedRoute.data = of({ etudiant });
      comp.ngOnInit();

      expect(comp.paysSharedCollection).toContainEqual(pays);
      expect(comp.etudiant).toEqual(etudiant);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEtudiant>>();
      const etudiant = { id: 3396 };
      jest.spyOn(etudiantFormService, 'getEtudiant').mockReturnValue(etudiant);
      jest.spyOn(etudiantService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ etudiant });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: etudiant }));
      saveSubject.complete();

      // THEN
      expect(etudiantFormService.getEtudiant).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(etudiantService.update).toHaveBeenCalledWith(expect.objectContaining(etudiant));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEtudiant>>();
      const etudiant = { id: 3396 };
      jest.spyOn(etudiantFormService, 'getEtudiant').mockReturnValue({ id: null });
      jest.spyOn(etudiantService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ etudiant: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: etudiant }));
      saveSubject.complete();

      // THEN
      expect(etudiantFormService.getEtudiant).toHaveBeenCalled();
      expect(etudiantService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEtudiant>>();
      const etudiant = { id: 3396 };
      jest.spyOn(etudiantService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ etudiant });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(etudiantService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePays', () => {
      it('should forward to paysService', () => {
        const entity = { id: 21471 };
        const entity2 = { id: 30122 };
        jest.spyOn(paysService, 'comparePays');
        comp.comparePays(entity, entity2);
        expect(paysService.comparePays).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
