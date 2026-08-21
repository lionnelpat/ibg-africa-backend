import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { TypeTacheDetailComponent } from './type-tache-detail.component';

describe('TypeTache Management Detail Component', () => {
  let comp: TypeTacheDetailComponent;
  let fixture: ComponentFixture<TypeTacheDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TypeTacheDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./type-tache-detail.component').then(m => m.TypeTacheDetailComponent),
              resolve: { typeTache: () => of({ id: 8191 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(TypeTacheDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TypeTacheDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load typeTache on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TypeTacheDetailComponent);

      // THEN
      expect(instance.typeTache()).toEqual(expect.objectContaining({ id: 8191 }));
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
