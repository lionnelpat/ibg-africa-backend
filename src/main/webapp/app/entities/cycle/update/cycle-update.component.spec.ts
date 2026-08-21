import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ICentreFormation } from 'app/entities/centre-formation/centre-formation.model';
import { CentreFormationService } from 'app/entities/centre-formation/service/centre-formation.service';
import { CycleService } from '../service/cycle.service';
import { ICycle } from '../cycle.model';
import { CycleFormService } from './cycle-form.service';

import { CycleUpdateComponent } from './cycle-update.component';

describe('Cycle Management Update Component', () => {
  let comp: CycleUpdateComponent;
  let fixture: ComponentFixture<CycleUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let cycleFormService: CycleFormService;
  let cycleService: CycleService;
  let centreFormationService: CentreFormationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CycleUpdateComponent],
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
      .overrideTemplate(CycleUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CycleUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cycleFormService = TestBed.inject(CycleFormService);
    cycleService = TestBed.inject(CycleService);
    centreFormationService = TestBed.inject(CentreFormationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call CentreFormation query and add missing value', () => {
      const cycle: ICycle = { id: 31548 };
      const centre: ICentreFormation = { id: 18876 };
      cycle.centre = centre;

      const centreFormationCollection: ICentreFormation[] = [{ id: 18876 }];
      jest.spyOn(centreFormationService, 'query').mockReturnValue(of(new HttpResponse({ body: centreFormationCollection })));
      const additionalCentreFormations = [centre];
      const expectedCollection: ICentreFormation[] = [...additionalCentreFormations, ...centreFormationCollection];
      jest.spyOn(centreFormationService, 'addCentreFormationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ cycle });
      comp.ngOnInit();

      expect(centreFormationService.query).toHaveBeenCalled();
      expect(centreFormationService.addCentreFormationToCollectionIfMissing).toHaveBeenCalledWith(
        centreFormationCollection,
        ...additionalCentreFormations.map(expect.objectContaining),
      );
      expect(comp.centreFormationsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const cycle: ICycle = { id: 31548 };
      const centre: ICentreFormation = { id: 18876 };
      cycle.centre = centre;

      activatedRoute.data = of({ cycle });
      comp.ngOnInit();

      expect(comp.centreFormationsSharedCollection).toContainEqual(centre);
      expect(comp.cycle).toEqual(cycle);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICycle>>();
      const cycle = { id: 8515 };
      jest.spyOn(cycleFormService, 'getCycle').mockReturnValue(cycle);
      jest.spyOn(cycleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cycle });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cycle }));
      saveSubject.complete();

      // THEN
      expect(cycleFormService.getCycle).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(cycleService.update).toHaveBeenCalledWith(expect.objectContaining(cycle));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICycle>>();
      const cycle = { id: 8515 };
      jest.spyOn(cycleFormService, 'getCycle').mockReturnValue({ id: null });
      jest.spyOn(cycleService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cycle: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cycle }));
      saveSubject.complete();

      // THEN
      expect(cycleFormService.getCycle).toHaveBeenCalled();
      expect(cycleService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICycle>>();
      const cycle = { id: 8515 };
      jest.spyOn(cycleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cycle });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cycleService.update).toHaveBeenCalled();
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
  });
});
