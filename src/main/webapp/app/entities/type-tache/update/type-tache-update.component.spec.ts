import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { TypeTacheService } from '../service/type-tache.service';
import { ITypeTache } from '../type-tache.model';
import { TypeTacheFormService } from './type-tache-form.service';

import { TypeTacheUpdateComponent } from './type-tache-update.component';

describe('TypeTache Management Update Component', () => {
  let comp: TypeTacheUpdateComponent;
  let fixture: ComponentFixture<TypeTacheUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let typeTacheFormService: TypeTacheFormService;
  let typeTacheService: TypeTacheService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [TypeTacheUpdateComponent],
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
      .overrideTemplate(TypeTacheUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TypeTacheUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    typeTacheFormService = TestBed.inject(TypeTacheFormService);
    typeTacheService = TestBed.inject(TypeTacheService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const typeTache: ITypeTache = { id: 2439 };

      activatedRoute.data = of({ typeTache });
      comp.ngOnInit();

      expect(comp.typeTache).toEqual(typeTache);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITypeTache>>();
      const typeTache = { id: 8191 };
      jest.spyOn(typeTacheFormService, 'getTypeTache').mockReturnValue(typeTache);
      jest.spyOn(typeTacheService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeTache });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: typeTache }));
      saveSubject.complete();

      // THEN
      expect(typeTacheFormService.getTypeTache).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(typeTacheService.update).toHaveBeenCalledWith(expect.objectContaining(typeTache));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITypeTache>>();
      const typeTache = { id: 8191 };
      jest.spyOn(typeTacheFormService, 'getTypeTache').mockReturnValue({ id: null });
      jest.spyOn(typeTacheService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeTache: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: typeTache }));
      saveSubject.complete();

      // THEN
      expect(typeTacheFormService.getTypeTache).toHaveBeenCalled();
      expect(typeTacheService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITypeTache>>();
      const typeTache = { id: 8191 };
      jest.spyOn(typeTacheService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ typeTache });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(typeTacheService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
