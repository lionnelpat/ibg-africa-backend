import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ICycle } from 'app/entities/cycle/cycle.model';
import { CycleService } from 'app/entities/cycle/service/cycle.service';
import { IEtudiant } from 'app/entities/etudiant/etudiant.model';
import { EtudiantService } from 'app/entities/etudiant/service/etudiant.service';
import { IInscriptionCycle } from '../inscription-cycle.model';
import { InscriptionCycleService } from '../service/inscription-cycle.service';
import { InscriptionCycleFormService } from './inscription-cycle-form.service';

import { InscriptionCycleUpdateComponent } from './inscription-cycle-update.component';

describe('InscriptionCycle Management Update Component', () => {
  let comp: InscriptionCycleUpdateComponent;
  let fixture: ComponentFixture<InscriptionCycleUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let inscriptionCycleFormService: InscriptionCycleFormService;
  let inscriptionCycleService: InscriptionCycleService;
  let cycleService: CycleService;
  let etudiantService: EtudiantService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [InscriptionCycleUpdateComponent],
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
      .overrideTemplate(InscriptionCycleUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InscriptionCycleUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    inscriptionCycleFormService = TestBed.inject(InscriptionCycleFormService);
    inscriptionCycleService = TestBed.inject(InscriptionCycleService);
    cycleService = TestBed.inject(CycleService);
    etudiantService = TestBed.inject(EtudiantService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Cycle query and add missing value', () => {
      const inscriptionCycle: IInscriptionCycle = { id: 8825 };
      const cycle: ICycle = { id: 8515 };
      inscriptionCycle.cycle = cycle;

      const cycleCollection: ICycle[] = [{ id: 8515 }];
      jest.spyOn(cycleService, 'query').mockReturnValue(of(new HttpResponse({ body: cycleCollection })));
      const additionalCycles = [cycle];
      const expectedCollection: ICycle[] = [...additionalCycles, ...cycleCollection];
      jest.spyOn(cycleService, 'addCycleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ inscriptionCycle });
      comp.ngOnInit();

      expect(cycleService.query).toHaveBeenCalled();
      expect(cycleService.addCycleToCollectionIfMissing).toHaveBeenCalledWith(
        cycleCollection,
        ...additionalCycles.map(expect.objectContaining),
      );
      expect(comp.cyclesSharedCollection).toEqual(expectedCollection);
    });

    it('should call Etudiant query and add missing value', () => {
      const inscriptionCycle: IInscriptionCycle = { id: 8825 };
      const etudiant: IEtudiant = { id: 3396 };
      inscriptionCycle.etudiant = etudiant;

      const etudiantCollection: IEtudiant[] = [{ id: 3396 }];
      jest.spyOn(etudiantService, 'query').mockReturnValue(of(new HttpResponse({ body: etudiantCollection })));
      const additionalEtudiants = [etudiant];
      const expectedCollection: IEtudiant[] = [...additionalEtudiants, ...etudiantCollection];
      jest.spyOn(etudiantService, 'addEtudiantToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ inscriptionCycle });
      comp.ngOnInit();

      expect(etudiantService.query).toHaveBeenCalled();
      expect(etudiantService.addEtudiantToCollectionIfMissing).toHaveBeenCalledWith(
        etudiantCollection,
        ...additionalEtudiants.map(expect.objectContaining),
      );
      expect(comp.etudiantsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const inscriptionCycle: IInscriptionCycle = { id: 8825 };
      const cycle: ICycle = { id: 8515 };
      inscriptionCycle.cycle = cycle;
      const etudiant: IEtudiant = { id: 3396 };
      inscriptionCycle.etudiant = etudiant;

      activatedRoute.data = of({ inscriptionCycle });
      comp.ngOnInit();

      expect(comp.cyclesSharedCollection).toContainEqual(cycle);
      expect(comp.etudiantsSharedCollection).toContainEqual(etudiant);
      expect(comp.inscriptionCycle).toEqual(inscriptionCycle);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInscriptionCycle>>();
      const inscriptionCycle = { id: 5654 };
      jest.spyOn(inscriptionCycleFormService, 'getInscriptionCycle').mockReturnValue(inscriptionCycle);
      jest.spyOn(inscriptionCycleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inscriptionCycle });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: inscriptionCycle }));
      saveSubject.complete();

      // THEN
      expect(inscriptionCycleFormService.getInscriptionCycle).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(inscriptionCycleService.update).toHaveBeenCalledWith(expect.objectContaining(inscriptionCycle));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInscriptionCycle>>();
      const inscriptionCycle = { id: 5654 };
      jest.spyOn(inscriptionCycleFormService, 'getInscriptionCycle').mockReturnValue({ id: null });
      jest.spyOn(inscriptionCycleService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inscriptionCycle: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: inscriptionCycle }));
      saveSubject.complete();

      // THEN
      expect(inscriptionCycleFormService.getInscriptionCycle).toHaveBeenCalled();
      expect(inscriptionCycleService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInscriptionCycle>>();
      const inscriptionCycle = { id: 5654 };
      jest.spyOn(inscriptionCycleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inscriptionCycle });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(inscriptionCycleService.update).toHaveBeenCalled();
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
