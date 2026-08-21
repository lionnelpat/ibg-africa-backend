import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IHabilitationCycle, NewHabilitationCycle } from '../habilitation-cycle.model';

export type PartialUpdateHabilitationCycle = Partial<IHabilitationCycle> & Pick<IHabilitationCycle, 'id'>;

type RestOf<T extends IHabilitationCycle | NewHabilitationCycle> = Omit<T, 'dateDebut' | 'dateFin'> & {
  dateDebut?: string | null;
  dateFin?: string | null;
};

export type RestHabilitationCycle = RestOf<IHabilitationCycle>;

export type NewRestHabilitationCycle = RestOf<NewHabilitationCycle>;

export type PartialUpdateRestHabilitationCycle = RestOf<PartialUpdateHabilitationCycle>;

export type EntityResponseType = HttpResponse<IHabilitationCycle>;
export type EntityArrayResponseType = HttpResponse<IHabilitationCycle[]>;

@Injectable({ providedIn: 'root' })
export class HabilitationCycleService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/habilitation-cycles');

  create(habilitationCycle: NewHabilitationCycle): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(habilitationCycle);
    return this.http
      .post<RestHabilitationCycle>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(habilitationCycle: IHabilitationCycle): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(habilitationCycle);
    return this.http
      .put<RestHabilitationCycle>(`${this.resourceUrl}/${this.getHabilitationCycleIdentifier(habilitationCycle)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(habilitationCycle: PartialUpdateHabilitationCycle): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(habilitationCycle);
    return this.http
      .patch<RestHabilitationCycle>(`${this.resourceUrl}/${this.getHabilitationCycleIdentifier(habilitationCycle)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestHabilitationCycle>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestHabilitationCycle[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getHabilitationCycleIdentifier(habilitationCycle: Pick<IHabilitationCycle, 'id'>): number {
    return habilitationCycle.id;
  }

  compareHabilitationCycle(o1: Pick<IHabilitationCycle, 'id'> | null, o2: Pick<IHabilitationCycle, 'id'> | null): boolean {
    return o1 && o2 ? this.getHabilitationCycleIdentifier(o1) === this.getHabilitationCycleIdentifier(o2) : o1 === o2;
  }

  addHabilitationCycleToCollectionIfMissing<Type extends Pick<IHabilitationCycle, 'id'>>(
    habilitationCycleCollection: Type[],
    ...habilitationCyclesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const habilitationCycles: Type[] = habilitationCyclesToCheck.filter(isPresent);
    if (habilitationCycles.length > 0) {
      const habilitationCycleCollectionIdentifiers = habilitationCycleCollection.map(habilitationCycleItem =>
        this.getHabilitationCycleIdentifier(habilitationCycleItem),
      );
      const habilitationCyclesToAdd = habilitationCycles.filter(habilitationCycleItem => {
        const habilitationCycleIdentifier = this.getHabilitationCycleIdentifier(habilitationCycleItem);
        if (habilitationCycleCollectionIdentifiers.includes(habilitationCycleIdentifier)) {
          return false;
        }
        habilitationCycleCollectionIdentifiers.push(habilitationCycleIdentifier);
        return true;
      });
      return [...habilitationCyclesToAdd, ...habilitationCycleCollection];
    }
    return habilitationCycleCollection;
  }

  protected convertDateFromClient<T extends IHabilitationCycle | NewHabilitationCycle | PartialUpdateHabilitationCycle>(
    habilitationCycle: T,
  ): RestOf<T> {
    return {
      ...habilitationCycle,
      dateDebut: habilitationCycle.dateDebut?.format(DATE_FORMAT) ?? null,
      dateFin: habilitationCycle.dateFin?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restHabilitationCycle: RestHabilitationCycle): IHabilitationCycle {
    return {
      ...restHabilitationCycle,
      dateDebut: restHabilitationCycle.dateDebut ? dayjs(restHabilitationCycle.dateDebut) : undefined,
      dateFin: restHabilitationCycle.dateFin ? dayjs(restHabilitationCycle.dateFin) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestHabilitationCycle>): HttpResponse<IHabilitationCycle> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestHabilitationCycle[]>): HttpResponse<IHabilitationCycle[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
