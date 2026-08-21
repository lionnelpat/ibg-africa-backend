import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { CoursService } from '../service/cours.service';
import { ICours } from '../cours.model';
import { CoursFormService } from './cours-form.service';

import { CoursUpdateComponent } from './cours-update.component';

describe('Cours Management Update Component', () => {
  let comp: CoursUpdateComponent;
  let fixture: ComponentFixture<CoursUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let coursFormService: CoursFormService;
  let coursService: CoursService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CoursUpdateComponent],
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
      .overrideTemplate(CoursUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CoursUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    coursFormService = TestBed.inject(CoursFormService);
    coursService = TestBed.inject(CoursService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const cours: ICours = { id: 6829 };

      activatedRoute.data = of({ cours });
      comp.ngOnInit();

      expect(comp.cours).toEqual(cours);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICours>>();
      const cours = { id: 387 };
      jest.spyOn(coursFormService, 'getCours').mockReturnValue(cours);
      jest.spyOn(coursService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cours });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cours }));
      saveSubject.complete();

      // THEN
      expect(coursFormService.getCours).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(coursService.update).toHaveBeenCalledWith(expect.objectContaining(cours));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICours>>();
      const cours = { id: 387 };
      jest.spyOn(coursFormService, 'getCours').mockReturnValue({ id: null });
      jest.spyOn(coursService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cours: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cours }));
      saveSubject.complete();

      // THEN
      expect(coursFormService.getCours).toHaveBeenCalled();
      expect(coursService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICours>>();
      const cours = { id: 387 };
      jest.spyOn(coursService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cours });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(coursService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
