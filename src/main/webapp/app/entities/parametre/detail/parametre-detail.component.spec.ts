import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ParametreDetailComponent } from './parametre-detail.component';

describe('Parametre Management Detail Component', () => {
  let comp: ParametreDetailComponent;
  let fixture: ComponentFixture<ParametreDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ParametreDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./parametre-detail.component').then(m => m.ParametreDetailComponent),
              resolve: { parametre: () => of({ id: 6734 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ParametreDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ParametreDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load parametre on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ParametreDetailComponent);

      // THEN
      expect(instance.parametre()).toEqual(expect.objectContaining({ id: 6734 }));
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
