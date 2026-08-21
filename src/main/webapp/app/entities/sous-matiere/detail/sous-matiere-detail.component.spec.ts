import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { SousMatiereDetailComponent } from './sous-matiere-detail.component';

describe('SousMatiere Management Detail Component', () => {
  let comp: SousMatiereDetailComponent;
  let fixture: ComponentFixture<SousMatiereDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SousMatiereDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./sous-matiere-detail.component').then(m => m.SousMatiereDetailComponent),
              resolve: { sousMatiere: () => of({ id: 5044 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(SousMatiereDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SousMatiereDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load sousMatiere on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', SousMatiereDetailComponent);

      // THEN
      expect(instance.sousMatiere()).toEqual(expect.objectContaining({ id: 5044 }));
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
