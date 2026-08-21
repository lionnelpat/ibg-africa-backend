import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ICycle } from 'app/entities/cycle/cycle.model';
import { CycleService } from 'app/entities/cycle/service/cycle.service';
import { IEnseignant } from 'app/entities/enseignant/enseignant.model';
import { EnseignantService } from 'app/entities/enseignant/service/enseignant.service';
import { IMatiere } from 'app/entities/matiere/matiere.model';
import { MatiereService } from 'app/entities/matiere/service/matiere.service';
import { ISousMatiere } from 'app/entities/sous-matiere/sous-matiere.model';
import { SousMatiereService } from 'app/entities/sous-matiere/service/sous-matiere.service';
import { ICours } from 'app/entities/cours/cours.model';
import { CoursService } from 'app/entities/cours/service/cours.service';
import { ITypeTache } from 'app/entities/type-tache/type-tache.model';
import { TypeTacheService } from 'app/entities/type-tache/service/type-tache.service';
import { IEvaluationPrevue } from '../evaluation-prevue.model';
import { EvaluationPrevueService } from '../service/evaluation-prevue.service';
import { EvaluationPrevueFormService } from './evaluation-prevue-form.service';

import { EvaluationPrevueUpdateComponent } from './evaluation-prevue-update.component';

describe('EvaluationPrevue Management Update Component', () => {
  let comp: EvaluationPrevueUpdateComponent;
  let fixture: ComponentFixture<EvaluationPrevueUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let evaluationPrevueFormService: EvaluationPrevueFormService;
  let evaluationPrevueService: EvaluationPrevueService;
  let cycleService: CycleService;
  let enseignantService: EnseignantService;
  let matiereService: MatiereService;
  let sousMatiereService: SousMatiereService;
  let coursService: CoursService;
  let typeTacheService: TypeTacheService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [EvaluationPrevueUpdateComponent],
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
      .overrideTemplate(EvaluationPrevueUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EvaluationPrevueUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    evaluationPrevueFormService = TestBed.inject(EvaluationPrevueFormService);
    evaluationPrevueService = TestBed.inject(EvaluationPrevueService);
    cycleService = TestBed.inject(CycleService);
    enseignantService = TestBed.inject(EnseignantService);
    matiereService = TestBed.inject(MatiereService);
    sousMatiereService = TestBed.inject(SousMatiereService);
    coursService = TestBed.inject(CoursService);
    typeTacheService = TestBed.inject(TypeTacheService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Cycle query and add missing value', () => {
      const evaluationPrevue: IEvaluationPrevue = { id: 20754 };
      const cycle: ICycle = { id: 8515 };
      evaluationPrevue.cycle = cycle;

      const cycleCollection: ICycle[] = [{ id: 8515 }];
      jest.spyOn(cycleService, 'query').mockReturnValue(of(new HttpResponse({ body: cycleCollection })));
      const additionalCycles = [cycle];
      const expectedCollection: ICycle[] = [...additionalCycles, ...cycleCollection];
      jest.spyOn(cycleService, 'addCycleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ evaluationPrevue });
      comp.ngOnInit();

      expect(cycleService.query).toHaveBeenCalled();
      expect(cycleService.addCycleToCollectionIfMissing).toHaveBeenCalledWith(
        cycleCollection,
        ...additionalCycles.map(expect.objectContaining),
      );
      expect(comp.cyclesSharedCollection).toEqual(expectedCollection);
    });

    it('should call Enseignant query and add missing value', () => {
      const evaluationPrevue: IEvaluationPrevue = { id: 20754 };
      const enseignant: IEnseignant = { id: 28512 };
      evaluationPrevue.enseignant = enseignant;

      const enseignantCollection: IEnseignant[] = [{ id: 28512 }];
      jest.spyOn(enseignantService, 'query').mockReturnValue(of(new HttpResponse({ body: enseignantCollection })));
      const additionalEnseignants = [enseignant];
      const expectedCollection: IEnseignant[] = [...additionalEnseignants, ...enseignantCollection];
      jest.spyOn(enseignantService, 'addEnseignantToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ evaluationPrevue });
      comp.ngOnInit();

      expect(enseignantService.query).toHaveBeenCalled();
      expect(enseignantService.addEnseignantToCollectionIfMissing).toHaveBeenCalledWith(
        enseignantCollection,
        ...additionalEnseignants.map(expect.objectContaining),
      );
      expect(comp.enseignantsSharedCollection).toEqual(expectedCollection);
    });

    it('should call Matiere query and add missing value', () => {
      const evaluationPrevue: IEvaluationPrevue = { id: 20754 };
      const matiere: IMatiere = { id: 30656 };
      evaluationPrevue.matiere = matiere;

      const matiereCollection: IMatiere[] = [{ id: 30656 }];
      jest.spyOn(matiereService, 'query').mockReturnValue(of(new HttpResponse({ body: matiereCollection })));
      const additionalMatieres = [matiere];
      const expectedCollection: IMatiere[] = [...additionalMatieres, ...matiereCollection];
      jest.spyOn(matiereService, 'addMatiereToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ evaluationPrevue });
      comp.ngOnInit();

      expect(matiereService.query).toHaveBeenCalled();
      expect(matiereService.addMatiereToCollectionIfMissing).toHaveBeenCalledWith(
        matiereCollection,
        ...additionalMatieres.map(expect.objectContaining),
      );
      expect(comp.matieresSharedCollection).toEqual(expectedCollection);
    });

    it('should call SousMatiere query and add missing value', () => {
      const evaluationPrevue: IEvaluationPrevue = { id: 20754 };
      const sousMatiere: ISousMatiere = { id: 5044 };
      evaluationPrevue.sousMatiere = sousMatiere;

      const sousMatiereCollection: ISousMatiere[] = [{ id: 5044 }];
      jest.spyOn(sousMatiereService, 'query').mockReturnValue(of(new HttpResponse({ body: sousMatiereCollection })));
      const additionalSousMatieres = [sousMatiere];
      const expectedCollection: ISousMatiere[] = [...additionalSousMatieres, ...sousMatiereCollection];
      jest.spyOn(sousMatiereService, 'addSousMatiereToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ evaluationPrevue });
      comp.ngOnInit();

      expect(sousMatiereService.query).toHaveBeenCalled();
      expect(sousMatiereService.addSousMatiereToCollectionIfMissing).toHaveBeenCalledWith(
        sousMatiereCollection,
        ...additionalSousMatieres.map(expect.objectContaining),
      );
      expect(comp.sousMatieresSharedCollection).toEqual(expectedCollection);
    });

    it('should call Cours query and add missing value', () => {
      const evaluationPrevue: IEvaluationPrevue = { id: 20754 };
      const cours: ICours = { id: 387 };
      evaluationPrevue.cours = cours;

      const coursCollection: ICours[] = [{ id: 387 }];
      jest.spyOn(coursService, 'query').mockReturnValue(of(new HttpResponse({ body: coursCollection })));
      const additionalCours = [cours];
      const expectedCollection: ICours[] = [...additionalCours, ...coursCollection];
      jest.spyOn(coursService, 'addCoursToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ evaluationPrevue });
      comp.ngOnInit();

      expect(coursService.query).toHaveBeenCalled();
      expect(coursService.addCoursToCollectionIfMissing).toHaveBeenCalledWith(
        coursCollection,
        ...additionalCours.map(expect.objectContaining),
      );
      expect(comp.coursSharedCollection).toEqual(expectedCollection);
    });

    it('should call TypeTache query and add missing value', () => {
      const evaluationPrevue: IEvaluationPrevue = { id: 20754 };
      const typeTache: ITypeTache = { id: 8191 };
      evaluationPrevue.typeTache = typeTache;

      const typeTacheCollection: ITypeTache[] = [{ id: 8191 }];
      jest.spyOn(typeTacheService, 'query').mockReturnValue(of(new HttpResponse({ body: typeTacheCollection })));
      const additionalTypeTaches = [typeTache];
      const expectedCollection: ITypeTache[] = [...additionalTypeTaches, ...typeTacheCollection];
      jest.spyOn(typeTacheService, 'addTypeTacheToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ evaluationPrevue });
      comp.ngOnInit();

      expect(typeTacheService.query).toHaveBeenCalled();
      expect(typeTacheService.addTypeTacheToCollectionIfMissing).toHaveBeenCalledWith(
        typeTacheCollection,
        ...additionalTypeTaches.map(expect.objectContaining),
      );
      expect(comp.typeTachesSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const evaluationPrevue: IEvaluationPrevue = { id: 20754 };
      const cycle: ICycle = { id: 8515 };
      evaluationPrevue.cycle = cycle;
      const enseignant: IEnseignant = { id: 28512 };
      evaluationPrevue.enseignant = enseignant;
      const matiere: IMatiere = { id: 30656 };
      evaluationPrevue.matiere = matiere;
      const sousMatiere: ISousMatiere = { id: 5044 };
      evaluationPrevue.sousMatiere = sousMatiere;
      const cours: ICours = { id: 387 };
      evaluationPrevue.cours = cours;
      const typeTache: ITypeTache = { id: 8191 };
      evaluationPrevue.typeTache = typeTache;

      activatedRoute.data = of({ evaluationPrevue });
      comp.ngOnInit();

      expect(comp.cyclesSharedCollection).toContainEqual(cycle);
      expect(comp.enseignantsSharedCollection).toContainEqual(enseignant);
      expect(comp.matieresSharedCollection).toContainEqual(matiere);
      expect(comp.sousMatieresSharedCollection).toContainEqual(sousMatiere);
      expect(comp.coursSharedCollection).toContainEqual(cours);
      expect(comp.typeTachesSharedCollection).toContainEqual(typeTache);
      expect(comp.evaluationPrevue).toEqual(evaluationPrevue);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEvaluationPrevue>>();
      const evaluationPrevue = { id: 11034 };
      jest.spyOn(evaluationPrevueFormService, 'getEvaluationPrevue').mockReturnValue(evaluationPrevue);
      jest.spyOn(evaluationPrevueService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ evaluationPrevue });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: evaluationPrevue }));
      saveSubject.complete();

      // THEN
      expect(evaluationPrevueFormService.getEvaluationPrevue).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(evaluationPrevueService.update).toHaveBeenCalledWith(expect.objectContaining(evaluationPrevue));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEvaluationPrevue>>();
      const evaluationPrevue = { id: 11034 };
      jest.spyOn(evaluationPrevueFormService, 'getEvaluationPrevue').mockReturnValue({ id: null });
      jest.spyOn(evaluationPrevueService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ evaluationPrevue: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: evaluationPrevue }));
      saveSubject.complete();

      // THEN
      expect(evaluationPrevueFormService.getEvaluationPrevue).toHaveBeenCalled();
      expect(evaluationPrevueService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEvaluationPrevue>>();
      const evaluationPrevue = { id: 11034 };
      jest.spyOn(evaluationPrevueService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ evaluationPrevue });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(evaluationPrevueService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCycle', () => {
      it('should forward to cycleService', () => {
        const entity = { id: 8515 };
        const entity2 = { id: 31548 };
        jest.spyOn(cycleService, 'compareCycle');
        comp.compareCycle(entity, entity2);
        expect(cycleService.compareCycle).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareEnseignant', () => {
      it('should forward to enseignantService', () => {
        const entity = { id: 28512 };
        const entity2 = { id: 11242 };
        jest.spyOn(enseignantService, 'compareEnseignant');
        comp.compareEnseignant(entity, entity2);
        expect(enseignantService.compareEnseignant).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareMatiere', () => {
      it('should forward to matiereService', () => {
        const entity = { id: 30656 };
        const entity2 = { id: 4963 };
        jest.spyOn(matiereService, 'compareMatiere');
        comp.compareMatiere(entity, entity2);
        expect(matiereService.compareMatiere).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareSousMatiere', () => {
      it('should forward to sousMatiereService', () => {
        const entity = { id: 5044 };
        const entity2 = { id: 7334 };
        jest.spyOn(sousMatiereService, 'compareSousMatiere');
        comp.compareSousMatiere(entity, entity2);
        expect(sousMatiereService.compareSousMatiere).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCours', () => {
      it('should forward to coursService', () => {
        const entity = { id: 387 };
        const entity2 = { id: 6829 };
        jest.spyOn(coursService, 'compareCours');
        comp.compareCours(entity, entity2);
        expect(coursService.compareCours).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareTypeTache', () => {
      it('should forward to typeTacheService', () => {
        const entity = { id: 8191 };
        const entity2 = { id: 2439 };
        jest.spyOn(typeTacheService, 'compareTypeTache');
        comp.compareTypeTache(entity, entity2);
        expect(typeTacheService.compareTypeTache).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
