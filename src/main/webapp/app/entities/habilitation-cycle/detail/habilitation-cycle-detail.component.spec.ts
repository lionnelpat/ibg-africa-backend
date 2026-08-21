import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { HabilitationCycleDetailComponent } from './habilitation-cycle-detail.component';

describe('HabilitationCycle Management Detail Component', () => {
  let comp: HabilitationCycleDetailComponent;
  let fixture: ComponentFixture<HabilitationCycleDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HabilitationCycleDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./habilitation-cycle-detail.component').then(m => m.HabilitationCycleDetailComponent),
              resolve: { habilitationCycle: () => of({ id: 3796 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(HabilitationCycleDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(HabilitationCycleDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load habilitationCycle on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', HabilitationCycleDetailComponent);

      // THEN
      expect(instance.habilitationCycle()).toEqual(expect.objectContaining({ id: 3796 }));
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
