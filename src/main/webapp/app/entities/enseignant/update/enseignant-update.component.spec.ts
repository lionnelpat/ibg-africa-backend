import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { EnseignantService } from '../service/enseignant.service';
import { IEnseignant } from '../enseignant.model';
import { EnseignantFormService } from './enseignant-form.service';

import { EnseignantUpdateComponent } from './enseignant-update.component';

describe('Enseignant Management Update Component', () => {
  let comp: EnseignantUpdateComponent;
  let fixture: ComponentFixture<EnseignantUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let enseignantFormService: EnseignantFormService;
  let enseignantService: EnseignantService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [EnseignantUpdateComponent],
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
      .overrideTemplate(EnseignantUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EnseignantUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    enseignantFormService = TestBed.inject(EnseignantFormService);
    enseignantService = TestBed.inject(EnseignantService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const enseignant: IEnseignant = { id: 11242 };

      activatedRoute.data = of({ enseignant });
      comp.ngOnInit();

      expect(comp.enseignant).toEqual(enseignant);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEnseignant>>();
      const enseignant = { id: 28512 };
      jest.spyOn(enseignantFormService, 'getEnseignant').mockReturnValue(enseignant);
      jest.spyOn(enseignantService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ enseignant });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: enseignant }));
      saveSubject.complete();

      // THEN
      expect(enseignantFormService.getEnseignant).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(enseignantService.update).toHaveBeenCalledWith(expect.objectContaining(enseignant));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEnseignant>>();
      const enseignant = { id: 28512 };
      jest.spyOn(enseignantFormService, 'getEnseignant').mockReturnValue({ id: null });
      jest.spyOn(enseignantService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ enseignant: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: enseignant }));
      saveSubject.complete();

      // THEN
      expect(enseignantFormService.getEnseignant).toHaveBeenCalled();
      expect(enseignantService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEnseignant>>();
      const enseignant = { id: 28512 };
      jest.spyOn(enseignantService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ enseignant });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(enseignantService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
