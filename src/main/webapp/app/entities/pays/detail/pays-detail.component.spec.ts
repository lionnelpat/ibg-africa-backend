import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { PaysDetailComponent } from './pays-detail.component';

describe('Pays Management Detail Component', () => {
  let comp: PaysDetailComponent;
  let fixture: ComponentFixture<PaysDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PaysDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./pays-detail.component').then(m => m.PaysDetailComponent),
              resolve: { pays: () => of({ id: 21471 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(PaysDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PaysDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load pays on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PaysDetailComponent);

      // THEN
      expect(instance.pays()).toEqual(expect.objectContaining({ id: 21471 }));
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
