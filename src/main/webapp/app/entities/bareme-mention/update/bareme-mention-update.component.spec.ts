import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ICentreFormation } from 'app/entities/centre-formation/centre-formation.model';
import { CentreFormationService } from 'app/entities/centre-formation/service/centre-formation.service';
import { BaremeMentionService } from '../service/bareme-mention.service';
import { IBaremeMention } from '../bareme-mention.model';
import { BaremeMentionFormService } from './bareme-mention-form.service';

import { BaremeMentionUpdateComponent } from './bareme-mention-update.component';

describe('BaremeMention Management Update Component', () => {
  let comp: BaremeMentionUpdateComponent;
  let fixture: ComponentFixture<BaremeMentionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let baremeMentionFormService: BaremeMentionFormService;
  let baremeMentionService: BaremeMentionService;
  let centreFormationService: CentreFormationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [BaremeMentionUpdateComponent],
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
      .overrideTemplate(BaremeMentionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BaremeMentionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    baremeMentionFormService = TestBed.inject(BaremeMentionFormService);
    baremeMentionService = TestBed.inject(BaremeMentionService);
    centreFormationService = TestBed.inject(CentreFormationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call CentreFormation query and add missing value', () => {
      const baremeMention: IBaremeMention = { id: 32244 };
      const centre: ICentreFormation = { id: 18876 };
      baremeMention.centre = centre;

      const centreFormationCollection: ICentreFormation[] = [{ id: 18876 }];
      jest.spyOn(centreFormationService, 'query').mockReturnValue(of(new HttpResponse({ body: centreFormationCollection })));
      const additionalCentreFormations = [centre];
      const expectedCollection: ICentreFormation[] = [...additionalCentreFormations, ...centreFormationCollection];
      jest.spyOn(centreFormationService, 'addCentreFormationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ baremeMention });
      comp.ngOnInit();

      expect(centreFormationService.query).toHaveBeenCalled();
      expect(centreFormationService.addCentreFormationToCollectionIfMissing).toHaveBeenCalledWith(
        centreFormationCollection,
        ...additionalCentreFormations.map(expect.objectContaining),
      );
      expect(comp.centreFormationsSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const baremeMention: IBaremeMention = { id: 32244 };
      const centre: ICentreFormation = { id: 18876 };
      baremeMention.centre = centre;

      activatedRoute.data = of({ baremeMention });
      comp.ngOnInit();

      expect(comp.centreFormationsSharedCollection).toContainEqual(centre);
      expect(comp.baremeMention).toEqual(baremeMention);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBaremeMention>>();
      const baremeMention = { id: 25091 };
      jest.spyOn(baremeMentionFormService, 'getBaremeMention').mockReturnValue(baremeMention);
      jest.spyOn(baremeMentionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ baremeMention });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: baremeMention }));
      saveSubject.complete();

      // THEN
      expect(baremeMentionFormService.getBaremeMention).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(baremeMentionService.update).toHaveBeenCalledWith(expect.objectContaining(baremeMention));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBaremeMention>>();
      const baremeMention = { id: 25091 };
      jest.spyOn(baremeMentionFormService, 'getBaremeMention').mockReturnValue({ id: null });
      jest.spyOn(baremeMentionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ baremeMention: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: baremeMention }));
      saveSubject.complete();

      // THEN
      expect(baremeMentionFormService.getBaremeMention).toHaveBeenCalled();
      expect(baremeMentionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBaremeMention>>();
      const baremeMention = { id: 25091 };
      jest.spyOn(baremeMentionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ baremeMention });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(baremeMentionService.update).toHaveBeenCalled();
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
