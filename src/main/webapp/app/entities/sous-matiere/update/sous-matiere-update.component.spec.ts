import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { SousMatiereService } from '../service/sous-matiere.service';
import { ISousMatiere } from '../sous-matiere.model';
import { SousMatiereFormService } from './sous-matiere-form.service';

import { SousMatiereUpdateComponent } from './sous-matiere-update.component';

describe('SousMatiere Management Update Component', () => {
  let comp: SousMatiereUpdateComponent;
  let fixture: ComponentFixture<SousMatiereUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let sousMatiereFormService: SousMatiereFormService;
  let sousMatiereService: SousMatiereService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [SousMatiereUpdateComponent],
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
      .overrideTemplate(SousMatiereUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SousMatiereUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sousMatiereFormService = TestBed.inject(SousMatiereFormService);
    sousMatiereService = TestBed.inject(SousMatiereService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const sousMatiere: ISousMatiere = { id: 7334 };

      activatedRoute.data = of({ sousMatiere });
      comp.ngOnInit();

      expect(comp.sousMatiere).toEqual(sousMatiere);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISousMatiere>>();
      const sousMatiere = { id: 5044 };
      jest.spyOn(sousMatiereFormService, 'getSousMatiere').mockReturnValue(sousMatiere);
      jest.spyOn(sousMatiereService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sousMatiere });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sousMatiere }));
      saveSubject.complete();

      // THEN
      expect(sousMatiereFormService.getSousMatiere).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(sousMatiereService.update).toHaveBeenCalledWith(expect.objectContaining(sousMatiere));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISousMatiere>>();
      const sousMatiere = { id: 5044 };
      jest.spyOn(sousMatiereFormService, 'getSousMatiere').mockReturnValue({ id: null });
      jest.spyOn(sousMatiereService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sousMatiere: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sousMatiere }));
      saveSubject.complete();

      // THEN
      expect(sousMatiereFormService.getSousMatiere).toHaveBeenCalled();
      expect(sousMatiereService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISousMatiere>>();
      const sousMatiere = { id: 5044 };
      jest.spyOn(sousMatiereService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sousMatiere });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(sousMatiereService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
