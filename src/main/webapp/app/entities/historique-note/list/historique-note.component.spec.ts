import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse, provideHttpClient } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { HistoriqueNoteService } from '../service/historique-note.service';

import { HistoriqueNoteComponent } from './historique-note.component';
import SpyInstance = jest.SpyInstance;

describe('HistoriqueNote Management Component', () => {
  let comp: HistoriqueNoteComponent;
  let fixture: ComponentFixture<HistoriqueNoteComponent>;
  let service: HistoriqueNoteService;
  let routerNavigateSpy: SpyInstance<Promise<boolean>>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HistoriqueNoteComponent],
      providers: [
        provideHttpClient(),
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
                'filter[someId.in]': 'dc4279ea-cfb9-11ec-9d64-0242ac120002',
              }),
            ),
            snapshot: {
              queryParams: {},
              queryParamMap: jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
                'filter[someId.in]': 'dc4279ea-cfb9-11ec-9d64-0242ac120002',
              }),
            },
          },
        },
      ],
    })
      .overrideTemplate(HistoriqueNoteComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(HistoriqueNoteComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(HistoriqueNoteService);
    routerNavigateSpy = jest.spyOn(comp.router, 'navigate');

    jest
      .spyOn(service, 'query')
      .mockReturnValueOnce(
        of(
          new HttpResponse({
            body: [{ id: 3252 }],
            headers: new HttpHeaders({
              link: '<http://localhost/api/foo?page=1&size=20>; rel="next"',
            }),
          }),
        ),
      )
      .mockReturnValueOnce(
        of(
          new HttpResponse({
            body: [{ id: 4745 }],
            headers: new HttpHeaders({
              link: '<http://localhost/api/foo?page=0&size=20>; rel="prev",<http://localhost/api/foo?page=2&size=20>; rel="next"',
            }),
          }),
        ),
      );
  });

  it('should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.historiqueNotes()[0]).toEqual(expect.objectContaining({ id: 3252 }));
  });

  describe('trackId', () => {
    it('should forward to historiqueNoteService', () => {
      const entity = { id: 3252 };
      jest.spyOn(service, 'getHistoriqueNoteIdentifier');
      const id = comp.trackId(entity);
      expect(service.getHistoriqueNoteIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });

  it('should calculate the sort attribute for a non-id attribute', () => {
    // WHEN
    comp.navigateToWithComponentValues({ predicate: 'non-existing-column', order: 'asc' });

    // THEN
    expect(routerNavigateSpy).toHaveBeenLastCalledWith(
      expect.anything(),
      expect.objectContaining({
        queryParams: expect.objectContaining({
          sort: ['non-existing-column,asc'],
        }),
      }),
    );
  });

  it('should calculate the sort attribute for an id', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenLastCalledWith(expect.objectContaining({ sort: ['id,desc'] }));
  });

  it('should infinite scroll', () => {
    // GIVEN
    comp.loadNextPage();
    comp.loadNextPage();
    comp.loadNextPage();

    // THEN
    expect(service.query).toHaveBeenCalledTimes(3);
    expect(service.query).toHaveBeenNthCalledWith(2, expect.objectContaining({ page: '1' }));
    expect(service.query).toHaveBeenLastCalledWith(expect.objectContaining({ page: '2' }));
  });
});
