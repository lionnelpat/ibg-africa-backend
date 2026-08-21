import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ICentreFormation } from 'app/entities/centre-formation/centre-formation.model';
import { CentreFormationService } from 'app/entities/centre-formation/service/centre-formation.service';
import { ParametreService } from '../service/parametre.service';
import { IParametre } from '../parametre.model';
import { ParametreFormService } from './parametre-form.service';

import { ParametreUpdateComponent } from './parametre-update.component';

describe('Parametre Management Update Component', () => {
  let comp: ParametreUpdateComponent;
  let fixture: ComponentFixture<ParametreUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let parametreFormService: ParametreFormService;
  let parametreService: ParametreService;
  let centreFormationService: CentreFormationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ParametreUpdateComponent],
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
      .overrideTemplate(ParametreUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ParametreUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    parametreFormService = TestBed.inject(ParametreFormService);
    parametreService = TestBed.inject(ParametreService);
    centreFormationService = TestBed.inject(CentreFormationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call CentreFormation query and add missing value', () => {
      const parametre: IParametre = { id: 17145 };
      const centre: ICentreFormation = { id: 18876 };
      parametre.centre = centre;

      const centreFormationCollection: ICentreFormation[] = [{ id: 18876 }];
      jest.spyOn(centreFormationService, 'query').mockReturnValue(of(new HttpResponse({ body: centreFormationCollection })));
      const additionalCentreFormations = [centre];
      const expectedCollection: ICentreFormation[] = [...additionalCentreFormations, ...centreFormationCollection];
      jest.spyOn(centreFormationService, 'addCentreFormationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ parametre });
      comp.ngOnInit();

      expect(centreFormationService.query).toHaveBeenCalled();
      expect(centreFormationService.addCentreFormationToCollectionIfMissing).toHaveBeenCalledWith(
        centreFormationCollection,
        ...additionalCentreFormations.map(expect.objectContaining),
      );
      expect(comp.centreFormationsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const parametre: IParametre = { id: 17145 };
      const centre: ICentreFormation = { id: 18876 };
      parametre.centre = centre;

      activatedRoute.data = of({ parametre });
      comp.ngOnInit();

      expect(comp.centreFormationsSharedCollection).toContainEqual(centre);
      expect(comp.parametre).toEqual(parametre);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IParametre>>();
      const parametre = { id: 6734 };
      jest.spyOn(parametreFormService, 'getParametre').mockReturnValue(parametre);
      jest.spyOn(parametreService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ parametre });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: parametre }));
      saveSubject.complete();

      // THEN
      expect(parametreFormService.getParametre).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(parametreService.update).toHaveBeenCalledWith(expect.objectContaining(parametre));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IParametre>>();
      const parametre = { id: 6734 };
      jest.spyOn(parametreFormService, 'getParametre').mockReturnValue({ id: null });
      jest.spyOn(parametreService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ parametre: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: parametre }));
      saveSubject.complete();

      // THEN
      expect(parametreFormService.getParametre).toHaveBeenCalled();
      expect(parametreService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IParametre>>();
      const parametre = { id: 6734 };
      jest.spyOn(parametreService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ parametre });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(parametreService.update).toHaveBeenCalled();
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
