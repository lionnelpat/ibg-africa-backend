import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { EvenementEtudiantDetailComponent } from './evenement-etudiant-detail.component';

describe('EvenementEtudiant Management Detail Component', () => {
  let comp: EvenementEtudiantDetailComponent;
  let fixture: ComponentFixture<EvenementEtudiantDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EvenementEtudiantDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./evenement-etudiant-detail.component').then(m => m.EvenementEtudiantDetailComponent),
              resolve: { evenementEtudiant: () => of({ id: 2800 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(EvenementEtudiantDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EvenementEtudiantDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load evenementEtudiant on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', EvenementEtudiantDetailComponent);

      // THEN
      expect(instance.evenementEtudiant()).toEqual(expect.objectContaining({ id: 2800 }));
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
