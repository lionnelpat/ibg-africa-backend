import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IEtudiant } from 'app/entities/etudiant/etudiant.model';
import { EtudiantService } from 'app/entities/etudiant/service/etudiant.service';
import { EvenementEtudiantService } from '../service/evenement-etudiant.service';
import { IEvenementEtudiant } from '../evenement-etudiant.model';
import { EvenementEtudiantFormService } from './evenement-etudiant-form.service';

import { EvenementEtudiantUpdateComponent } from './evenement-etudiant-update.component';

describe('EvenementEtudiant Management Update Component', () => {
  let comp: EvenementEtudiantUpdateComponent;
  let fixture: ComponentFixture<EvenementEtudiantUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let evenementEtudiantFormService: EvenementEtudiantFormService;
  let evenementEtudiantService: EvenementEtudiantService;
  let etudiantService: EtudiantService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [EvenementEtudiantUpdateComponent],
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
      .overrideTemplate(EvenementEtudiantUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EvenementEtudiantUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    evenementEtudiantFormService = TestBed.inject(EvenementEtudiantFormService);
    evenementEtudiantService = TestBed.inject(EvenementEtudiantService);
    etudiantService = TestBed.inject(EtudiantService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Etudiant query and add missing value', () => {
      const evenementEtudiant: IEvenementEtudiant = { id: 116 };
      const etudiant: IEtudiant = { id: 3396 };
      evenementEtudiant.etudiant = etudiant;

      const etudiantCollection: IEtudiant[] = [{ id: 3396 }];
      jest.spyOn(etudiantService, 'query').mockReturnValue(of(new HttpResponse({ body: etudiantCollection })));
      const additionalEtudiants = [etudiant];
      const expectedCollection: IEtudiant[] = [...additionalEtudiants, ...etudiantCollection];
      jest.spyOn(etudiantService, 'addEtudiantToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ evenementEtudiant });
      comp.ngOnInit();

      expect(etudiantService.query).toHaveBeenCalled();
      expect(etudiantService.addEtudiantToCollectionIfMissing).toHaveBeenCalledWith(
        etudiantCollection,
        ...additionalEtudiants.map(expect.objectContaining),
      );
      expect(comp.etudiantsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const evenementEtudiant: IEvenementEtudiant = { id: 116 };
      const etudiant: IEtudiant = { id: 3396 };
      evenementEtudiant.etudiant = etudiant;

      activatedRoute.data = of({ evenementEtudiant });
      comp.ngOnInit();

      expect(comp.etudiantsSharedCollection).toContainEqual(etudiant);
      expect(comp.evenementEtudiant).toEqual(evenementEtudiant);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEvenementEtudiant>>();
      const evenementEtudiant = { id: 2800 };
      jest.spyOn(evenementEtudiantFormService, 'getEvenementEtudiant').mockReturnValue(evenementEtudiant);
      jest.spyOn(evenementEtudiantService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ evenementEtudiant });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: evenementEtudiant }));
      saveSubject.complete();

      // THEN
      expect(evenementEtudiantFormService.getEvenementEtudiant).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(evenementEtudiantService.update).toHaveBeenCalledWith(expect.objectContaining(evenementEtudiant));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEvenementEtudiant>>();
      const evenementEtudiant = { id: 2800 };
      jest.spyOn(evenementEtudiantFormService, 'getEvenementEtudiant').mockReturnValue({ id: null });
      jest.spyOn(evenementEtudiantService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ evenementEtudiant: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: evenementEtudiant }));
      saveSubject.complete();

      // THEN
      expect(evenementEtudiantFormService.getEvenementEtudiant).toHaveBeenCalled();
      expect(evenementEtudiantService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEvenementEtudiant>>();
      const evenementEtudiant = { id: 2800 };
      jest.spyOn(evenementEtudiantService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ evenementEtudiant });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(evenementEtudiantService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareEtudiant', () => {
      it('should forward to etudiantService', () => {
        const entity = { id: 3396 };
        const entity2 = { id: 14632 };
        jest.spyOn(etudiantService, 'compareEtudiant');
        comp.compareEtudiant(entity, entity2);
        expect(etudiantService.compareEtudiant).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
