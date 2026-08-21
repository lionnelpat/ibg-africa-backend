import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IEvaluationPrevue } from 'app/entities/evaluation-prevue/evaluation-prevue.model';
import { EvaluationPrevueService } from 'app/entities/evaluation-prevue/service/evaluation-prevue.service';
import { IEtudiant } from 'app/entities/etudiant/etudiant.model';
import { EtudiantService } from 'app/entities/etudiant/service/etudiant.service';
import { IEvaluationRealisee } from '../evaluation-realisee.model';
import { EvaluationRealiseeService } from '../service/evaluation-realisee.service';
import { EvaluationRealiseeFormService } from './evaluation-realisee-form.service';

import { EvaluationRealiseeUpdateComponent } from './evaluation-realisee-update.component';

describe('EvaluationRealisee Management Update Component', () => {
  let comp: EvaluationRealiseeUpdateComponent;
  let fixture: ComponentFixture<EvaluationRealiseeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let evaluationRealiseeFormService: EvaluationRealiseeFormService;
  let evaluationRealiseeService: EvaluationRealiseeService;
  let evaluationPrevueService: EvaluationPrevueService;
  let etudiantService: EtudiantService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [EvaluationRealiseeUpdateComponent],
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
      .overrideTemplate(EvaluationRealiseeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EvaluationRealiseeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    evaluationRealiseeFormService = TestBed.inject(EvaluationRealiseeFormService);
    evaluationRealiseeService = TestBed.inject(EvaluationRealiseeService);
    evaluationPrevueService = TestBed.inject(EvaluationPrevueService);
    etudiantService = TestBed.inject(EtudiantService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call EvaluationPrevue query and add missing value', () => {
      const evaluationRealisee: IEvaluationRealisee = { id: 4126 };
      const evaluationPrevue: IEvaluationPrevue = { id: 11034 };
      evaluationRealisee.evaluationPrevue = evaluationPrevue;

      const evaluationPrevueCollection: IEvaluationPrevue[] = [{ id: 11034 }];
      jest.spyOn(evaluationPrevueService, 'query').mockReturnValue(of(new HttpResponse({ body: evaluationPrevueCollection })));
      const additionalEvaluationPrevues = [evaluationPrevue];
      const expectedCollection: IEvaluationPrevue[] = [...additionalEvaluationPrevues, ...evaluationPrevueCollection];
      jest.spyOn(evaluationPrevueService, 'addEvaluationPrevueToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ evaluationRealisee });
      comp.ngOnInit();

      expect(evaluationPrevueService.query).toHaveBeenCalled();
      expect(evaluationPrevueService.addEvaluationPrevueToCollectionIfMissing).toHaveBeenCalledWith(
        evaluationPrevueCollection,
        ...additionalEvaluationPrevues.map(expect.objectContaining),
      );
      expect(comp.evaluationPrevuesSharedCollection).toEqual(expectedCollection);
    });

    it('should call Etudiant query and add missing value', () => {
      const evaluationRealisee: IEvaluationRealisee = { id: 4126 };
      const etudiant: IEtudiant = { id: 3396 };
      evaluationRealisee.etudiant = etudiant;

      const etudiantCollection: IEtudiant[] = [{ id: 3396 }];
      jest.spyOn(etudiantService, 'query').mockReturnValue(of(new HttpResponse({ body: etudiantCollection })));
      const additionalEtudiants = [etudiant];
      const expectedCollection: IEtudiant[] = [...additionalEtudiants, ...etudiantCollection];
      jest.spyOn(etudiantService, 'addEtudiantToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ evaluationRealisee });
      comp.ngOnInit();

      expect(etudiantService.query).toHaveBeenCalled();
      expect(etudiantService.addEtudiantToCollectionIfMissing).toHaveBeenCalledWith(
        etudiantCollection,
        ...additionalEtudiants.map(expect.objectContaining),
      );
      expect(comp.etudiantsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const evaluationRealisee: IEvaluationRealisee = { id: 4126 };
      const evaluationPrevue: IEvaluationPrevue = { id: 11034 };
      evaluationRealisee.evaluationPrevue = evaluationPrevue;
      const etudiant: IEtudiant = { id: 3396 };
      evaluationRealisee.etudiant = etudiant;

      activatedRoute.data = of({ evaluationRealisee });
      comp.ngOnInit();

      expect(comp.evaluationPrevuesSharedCollection).toContainEqual(evaluationPrevue);
      expect(comp.etudiantsSharedCollection).toContainEqual(etudiant);
      expect(comp.evaluationRealisee).toEqual(evaluationRealisee);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEvaluationRealisee>>();
      const evaluationRealisee = { id: 11421 };
      jest.spyOn(evaluationRealiseeFormService, 'getEvaluationRealisee').mockReturnValue(evaluationRealisee);
      jest.spyOn(evaluationRealiseeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ evaluationRealisee });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: evaluationRealisee }));
      saveSubject.complete();

      // THEN
      expect(evaluationRealiseeFormService.getEvaluationRealisee).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(evaluationRealiseeService.update).toHaveBeenCalledWith(expect.objectContaining(evaluationRealisee));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEvaluationRealisee>>();
      const evaluationRealisee = { id: 11421 };
      jest.spyOn(evaluationRealiseeFormService, 'getEvaluationRealisee').mockReturnValue({ id: null });
      jest.spyOn(evaluationRealiseeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ evaluationRealisee: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: evaluationRealisee }));
      saveSubject.complete();

      // THEN
      expect(evaluationRealiseeFormService.getEvaluationRealisee).toHaveBeenCalled();
      expect(evaluationRealiseeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEvaluationRealisee>>();
      const evaluationRealisee = { id: 11421 };
      jest.spyOn(evaluationRealiseeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ evaluationRealisee });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(evaluationRealiseeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareEvaluationPrevue', () => {
      it('should forward to evaluationPrevueService', () => {
        const entity = { id: 11034 };
        const entity2 = { id: 20754 };
        jest.spyOn(evaluationPrevueService, 'compareEvaluationPrevue');
        comp.compareEvaluationPrevue(entity, entity2);
        expect(evaluationPrevueService.compareEvaluationPrevue).toHaveBeenCalledWith(entity, entity2);
      });
    });

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
