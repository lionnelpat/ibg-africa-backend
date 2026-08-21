import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { CoursDetailComponent } from './cours-detail.component';

describe('Cours Management Detail Component', () => {
  let comp: CoursDetailComponent;
  let fixture: ComponentFixture<CoursDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CoursDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./cours-detail.component').then(m => m.CoursDetailComponent),
              resolve: { cours: () => of({ id: 387 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(CoursDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CoursDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load cours on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CoursDetailComponent);

      // THEN
      expect(instance.cours()).toEqual(expect.objectContaining({ id: 387 }));
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
