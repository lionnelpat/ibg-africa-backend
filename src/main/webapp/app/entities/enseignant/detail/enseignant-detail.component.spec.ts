import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { EnseignantDetailComponent } from './enseignant-detail.component';

describe('Enseignant Management Detail Component', () => {
  let comp: EnseignantDetailComponent;
  let fixture: ComponentFixture<EnseignantDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EnseignantDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./enseignant-detail.component').then(m => m.EnseignantDetailComponent),
              resolve: { enseignant: () => of({ id: 28512 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(EnseignantDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EnseignantDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load enseignant on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', EnseignantDetailComponent);

      // THEN
      expect(instance.enseignant()).toEqual(expect.objectContaining({ id: 28512 }));
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
