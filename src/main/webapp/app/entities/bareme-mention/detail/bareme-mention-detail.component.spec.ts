import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { BaremeMentionDetailComponent } from './bareme-mention-detail.component';

describe('BaremeMention Management Detail Component', () => {
  let comp: BaremeMentionDetailComponent;
  let fixture: ComponentFixture<BaremeMentionDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BaremeMentionDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./bareme-mention-detail.component').then(m => m.BaremeMentionDetailComponent),
              resolve: { baremeMention: () => of({ id: 25091 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(BaremeMentionDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BaremeMentionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load baremeMention on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', BaremeMentionDetailComponent);

      // THEN
      expect(instance.baremeMention()).toEqual(expect.objectContaining({ id: 25091 }));
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
