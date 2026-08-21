import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ICentreFormation } from 'app/entities/centre-formation/centre-formation.model';
import { CentreFormationService } from 'app/entities/centre-formation/service/centre-formation.service';
import { ICycle } from 'app/entities/cycle/cycle.model';
import { CycleService } from 'app/entities/cycle/service/cycle.service';
import { IHabilitationCycle } from '../habilitation-cycle.model';
import { HabilitationCycleService } from '../service/habilitation-cycle.service';
import { HabilitationCycleFormService } from './habilitation-cycle-form.service';

import { HabilitationCycleUpdateComponent } from './habilitation-cycle-update.component';

describe('HabilitationCycle Management Update Component', () => {
  let comp: HabilitationCycleUpdateComponent;
  let fixture: ComponentFixture<HabilitationCycleUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let habilitationCycleFormService: HabilitationCycleFormService;
  let habilitationCycleService: HabilitationCycleService;
  let centreFormationService: CentreFormationService;
  let cycleService: CycleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HabilitationCycleUpdateComponent],
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
      .overrideTemplate(HabilitationCycleUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(HabilitationCycleUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    habilitationCycleFormService = TestBed.inject(HabilitationCycleFormService);
    habilitationCycleService = TestBed.inject(HabilitationCycleService);
    centreFormationService = TestBed.inject(CentreFormationService);
    cycleService = TestBed.inject(CycleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call CentreFormation query and add missing value', () => {
      const habilitationCycle: IHabilitationCycle = { id: 4619 };
      const centre: ICentreFormation = { id: 18876 };
      habilitationCycle.centre = centre;

      const centreFormationCollection: ICentreFormation[] = [{ id: 18876 }];
      jest.spyOn(centreFormationService, 'query').mockReturnValue(of(new HttpResponse({ body: centreFormationCollection })));
      const additionalCentreFormations = [centre];
      const expectedCollection: ICentreFormation[] = [...additionalCentreFormations, ...centreFormationCollection];
      jest.spyOn(centreFormationService, 'addCentreFormationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ habilitationCycle });
      comp.ngOnInit();

      expect(centreFormationService.query).toHaveBeenCalled();
      expect(centreFormationService.addCentreFormationToCollectionIfMissing).toHaveBeenCalledWith(
        centreFormationCollection,
        ...additionalCentreFormations.map(expect.objectContaining),
      );
      expect(comp.centreFormationsSharedCollection).toEqual(expectedCollection);
    });

    it('should call Cycle query and add missing value', () => {
      const habilitationCycle: IHabilitationCycle = { id: 4619 };
      const cycle: ICycle = { id: 8515 };
      habilitationCycle.cycle = cycle;

      const cycleCollection: ICycle[] = [{ id: 8515 }];
      jest.spyOn(cycleService, 'query').mockReturnValue(of(new HttpResponse({ body: cycleCollection })));
      const additionalCycles = [cycle];
      const expectedCollection: ICycle[] = [...additionalCycles, ...cycleCollection];
      jest.spyOn(cycleService, 'addCycleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ habilitationCycle });
      comp.ngOnInit();

      expect(cycleService.query).toHaveBeenCalled();
      expect(cycleService.addCycleToCollectionIfMissing).toHaveBeenCalledWith(
        cycleCollection,
        ...additionalCycles.map(expect.objectContaining),
      );
      expect(comp.cyclesSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const habilitationCycle: IHabilitationCycle = { id: 4619 };
      const centre: ICentreFormation = { id: 18876 };
      habilitationCycle.centre = centre;
      const cycle: ICycle = { id: 8515 };
      habilitationCycle.cycle = cycle;

      activatedRoute.data = of({ habilitationCycle });
      comp.ngOnInit();

      expect(comp.centreFormationsSharedCollection).toContainEqual(centre);
      expect(comp.cyclesSharedCollection).toContainEqual(cycle);
      expect(comp.habilitationCycle).toEqual(habilitationCycle);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHabilitationCycle>>();
      const habilitationCycle = { id: 3796 };
      jest.spyOn(habilitationCycleFormService, 'getHabilitationCycle').mockReturnValue(habilitationCycle);
      jest.spyOn(habilitationCycleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ habilitationCycle });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: habilitationCycle }));
      saveSubject.complete();

      // THEN
      expect(habilitationCycleFormService.getHabilitationCycle).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(habilitationCycleService.update).toHaveBeenCalledWith(expect.objectContaining(habilitationCycle));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHabilitationCycle>>();
      const habilitationCycle = { id: 3796 };
      jest.spyOn(habilitationCycleFormService, 'getHabilitationCycle').mockReturnValue({ id: null });
      jest.spyOn(habilitationCycleService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ habilitationCycle: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: habilitationCycle }));
      saveSubject.complete();

      // THEN
      expect(habilitationCycleFormService.getHabilitationCycle).toHaveBeenCalled();
      expect(habilitationCycleService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHabilitationCycle>>();
      const habilitationCycle = { id: 3796 };
      jest.spyOn(habilitationCycleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ habilitationCycle });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(habilitationCycleService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCentreFormation', () => {
      it('should forward to centreFormationService', () => {
        const entity = { id: 18876 };
        const entity2 = { id: 18439 };
        jest.spyOn(centreFormationService, 'compareCentreFormation');
        comp.compareCentreFormation(entity, entity2);
        expect(centreFormationService.compareCentreFormation).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCycle', () => {
      it('should forward to cycleService', () => {
        const entity = { id: 8515 };
        const entity2 = { id: 31548 };
        jest.spyOn(cycleService, 'compareCycle');
        comp.compareCycle(entity, entity2);
        expect(cycleService.compareCycle).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
