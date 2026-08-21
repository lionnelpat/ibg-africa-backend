import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IPays } from 'app/entities/pays/pays.model';
import { PaysService } from 'app/entities/pays/service/pays.service';
import { CentreFormationService } from '../service/centre-formation.service';
import { ICentreFormation } from '../centre-formation.model';
import { CentreFormationFormService } from './centre-formation-form.service';

import { CentreFormationUpdateComponent } from './centre-formation-update.component';

describe('CentreFormation Management Update Component', () => {
  let comp: CentreFormationUpdateComponent;
  let fixture: ComponentFixture<CentreFormationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let centreFormationFormService: CentreFormationFormService;
  let centreFormationService: CentreFormationService;
  let paysService: PaysService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CentreFormationUpdateComponent],
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
      .overrideTemplate(CentreFormationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CentreFormationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    centreFormationFormService = TestBed.inject(CentreFormationFormService);
    centreFormationService = TestBed.inject(CentreFormationService);
    paysService = TestBed.inject(PaysService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Pays query and add missing value', () => {
      const centreFormation: ICentreFormation = { id: 18439 };
      const pays: IPays = { id: 21471 };
      centreFormation.pays = pays;

      const paysCollection: IPays[] = [{ id: 21471 }];
      jest.spyOn(paysService, 'query').mockReturnValue(of(new HttpResponse({ body: paysCollection })));
      const additionalPays = [pays];
      const expectedCollection: IPays[] = [...additionalPays, ...paysCollection];
      jest.spyOn(paysService, 'addPaysToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ centreFormation });
      comp.ngOnInit();

      expect(paysService.query).toHaveBeenCalled();
      expect(paysService.addPaysToCollectionIfMissing).toHaveBeenCalledWith(paysCollection, ...additionalPays.map(expect.objectContaining));
      expect(comp.paysSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const centreFormation: ICentreFormation = { id: 18439 };
      const pays: IPays = { id: 21471 };
      centreFormation.pays = pays;

      activatedRoute.data = of({ centreFormation });
      comp.ngOnInit();

      expect(comp.paysSharedCollection).toContainEqual(pays);
      expect(comp.centreFormation).toEqual(centreFormation);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICentreFormation>>();
      const centreFormation = { id: 18876 };
      jest.spyOn(centreFormationFormService, 'getCentreFormation').mockReturnValue(centreFormation);
      jest.spyOn(centreFormationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ centreFormation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: centreFormation }));
      saveSubject.complete();

      // THEN
      expect(centreFormationFormService.getCentreFormation).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(centreFormationService.update).toHaveBeenCalledWith(expect.objectContaining(centreFormation));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICentreFormation>>();
      const centreFormation = { id: 18876 };
      jest.spyOn(centreFormationFormService, 'getCentreFormation').mockReturnValue({ id: null });
      jest.spyOn(centreFormationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ centreFormation: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: centreFormation }));
      saveSubject.complete();

      // THEN
      expect(centreFormationFormService.getCentreFormation).toHaveBeenCalled();
      expect(centreFormationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICentreFormation>>();
      const centreFormation = { id: 18876 };
      jest.spyOn(centreFormationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ centreFormation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(centreFormationService.update).toHaveBeenCalled();
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
