import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { PaysService } from '../service/pays.service';
import { IPays } from '../pays.model';
import { PaysFormService } from './pays-form.service';

import { PaysUpdateComponent } from './pays-update.component';

describe('Pays Management Update Component', () => {
  let comp: PaysUpdateComponent;
  let fixture: ComponentFixture<PaysUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let paysFormService: PaysFormService;
  let paysService: PaysService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [PaysUpdateComponent],
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
      .overrideTemplate(PaysUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PaysUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    paysFormService = TestBed.inject(PaysFormService);
    paysService = TestBed.inject(PaysService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should update editForm', () => {
      const pays: IPays = { id: 30122 };

      activatedRoute.data = of({ pays });
      comp.ngOnInit();

      expect(comp.pays).toEqual(pays);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPays>>();
      const pays = { id: 21471 };
      jest.spyOn(paysFormService, 'getPays').mockReturnValue(pays);
      jest.spyOn(paysService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pays });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pays }));
      saveSubject.complete();

      // THEN
      expect(paysFormService.getPays).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(paysService.update).toHaveBeenCalledWith(expect.objectContaining(pays));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPays>>();
      const pays = { id: 21471 };
      jest.spyOn(paysFormService, 'getPays').mockReturnValue({ id: null });
      jest.spyOn(paysService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pays: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pays }));
      saveSubject.complete();

      // THEN
      expect(paysFormService.getPays).toHaveBeenCalled();
      expect(paysService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPays>>();
      const pays = { id: 21471 };
      jest.spyOn(paysService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pays });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(paysService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
