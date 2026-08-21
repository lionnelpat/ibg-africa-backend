import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { CycleDetailComponent } from './cycle-detail.component';

describe('Cycle Management Detail Component', () => {
  let comp: CycleDetailComponent;
  let fixture: ComponentFixture<CycleDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CycleDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./cycle-detail.component').then(m => m.CycleDetailComponent),
              resolve: { cycle: () => of({ id: 8515 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(CycleDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CycleDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load cycle on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CycleDetailComponent);

      // THEN
      expect(instance.cycle()).toEqual(expect.objectContaining({ id: 8515 }));
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
