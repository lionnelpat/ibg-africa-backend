import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { EvaluationRealiseeDetailComponent } from './evaluation-realisee-detail.component';

describe('EvaluationRealisee Management Detail Component', () => {
  let comp: EvaluationRealiseeDetailComponent;
  let fixture: ComponentFixture<EvaluationRealiseeDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EvaluationRealiseeDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./evaluation-realisee-detail.component').then(m => m.EvaluationRealiseeDetailComponent),
              resolve: { evaluationRealisee: () => of({ id: 11421 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(EvaluationRealiseeDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EvaluationRealiseeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load evaluationRealisee on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', EvaluationRealiseeDetailComponent);

      // THEN
      expect(instance.evaluationRealisee()).toEqual(expect.objectContaining({ id: 11421 }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
