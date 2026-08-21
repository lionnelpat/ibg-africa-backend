jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, fakeAsync, inject, tick } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { EvaluationPrevueService } from '../service/evaluation-prevue.service';

import { EvaluationPrevueDeleteDialogComponent } from './evaluation-prevue-delete-dialog.component';

describe('EvaluationPrevue Management Delete Component', () => {
  let comp: EvaluationPrevueDeleteDialogComponent;
  let fixture: ComponentFixture<EvaluationPrevueDeleteDialogComponent>;
  let service: EvaluationPrevueService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [EvaluationPrevueDeleteDialogComponent],
      providers: [provideHttpClient(), NgbActiveModal],
    })
      .overrideTemplate(EvaluationPrevueDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EvaluationPrevueDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(EvaluationPrevueService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      }),
    ));

    it('should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
