import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { EvaluationPrevueDetailComponent } from './evaluation-prevue-detail.component';

describe('EvaluationPrevue Management Detail Component', () => {
  let comp: EvaluationPrevueDetailComponent;
  let fixture: ComponentFixture<EvaluationPrevueDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EvaluationPrevueDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./evaluation-prevue-detail.component').then(m => m.EvaluationPrevueDetailComponent),
              resolve: { evaluationPrevue: () => of({ id: 11034 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(EvaluationPrevueDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EvaluationPrevueDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load evaluationPrevue on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', EvaluationPrevueDetailComponent);

      // THEN
      expect(instance.evaluationPrevue()).toEqual(expect.objectContaining({ id: 11034 }));
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
