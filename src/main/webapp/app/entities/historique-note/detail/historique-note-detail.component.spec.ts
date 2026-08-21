import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { HistoriqueNoteDetailComponent } from './historique-note-detail.component';

describe('HistoriqueNote Management Detail Component', () => {
  let comp: HistoriqueNoteDetailComponent;
  let fixture: ComponentFixture<HistoriqueNoteDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HistoriqueNoteDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./historique-note-detail.component').then(m => m.HistoriqueNoteDetailComponent),
              resolve: { historiqueNote: () => of({ id: 3252 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(HistoriqueNoteDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(HistoriqueNoteDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load historiqueNote on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', HistoriqueNoteDetailComponent);

      // THEN
      expect(instance.historiqueNote()).toEqual(expect.objectContaining({ id: 3252 }));
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
