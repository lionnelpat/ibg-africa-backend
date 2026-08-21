import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { InscriptionCycleDetailComponent } from './inscription-cycle-detail.component';

describe('InscriptionCycle Management Detail Component', () => {
  let comp: InscriptionCycleDetailComponent;
  let fixture: ComponentFixture<InscriptionCycleDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InscriptionCycleDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./inscription-cycle-detail.component').then(m => m.InscriptionCycleDetailComponent),
              resolve: { inscriptionCycle: () => of({ id: 5654 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(InscriptionCycleDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(InscriptionCycleDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load inscriptionCycle on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', InscriptionCycleDetailComponent);

      // THEN
      expect(instance.inscriptionCycle()).toEqual(expect.objectContaining({ id: 5654 }));
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
