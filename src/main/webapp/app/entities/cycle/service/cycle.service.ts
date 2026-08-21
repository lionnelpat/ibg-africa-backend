import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICycle, NewCycle } from '../cycle.model';

export type PartialUpdateCycle = Partial<ICycle> & Pick<ICycle, 'id'>;

type RestOf<T extends ICycle | NewCycle> = Omit<T, 'dateDebut' | 'dateFin'> & {
  dateDebut?: string | null;
  dateFin?: string | null;
};

export type RestCycle = RestOf<ICycle>;

export type NewRestCycle = RestOf<NewCycle>;

export type PartialUpdateRestCycle = RestOf<PartialUpdateCycle>;

export type EntityResponseType = HttpResponse<ICycle>;
export type EntityArrayResponseType = HttpResponse<ICycle[]>;

@Injectable({ providedIn: 'root' })
export class CycleService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/cycles');

  create(cycle: NewCycle): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cycle);
    return this.http.post<RestCycle>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(cycle: ICycle): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cycle);
    return this.http
      .put<RestCycle>(`${this.resourceUrl}/${this.getCycleIdentifier(cycle)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(cycle: PartialUpdateCycle): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cycle);
    return this.http
      .patch<RestCycle>(`${this.resourceUrl}/${this.getCycleIdentifier(cycle)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestCycle>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestCycle[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCycleIdentifier(cycle: Pick<ICycle, 'id'>): number {
    return cycle.id;
  }

  compareCycle(o1: Pick<ICycle, 'id'> | null, o2: Pick<ICycle, 'id'> | null): boolean {
    return o1 && o2 ? this.getCycleIdentifier(o1) === this.getCycleIdentifier(o2) : o1 === o2;
  }

  addCycleToCollectionIfMissing<Type extends Pick<ICycle, 'id'>>(
    cycleCollection: Type[],
    ...cyclesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const cycles: Type[] = cyclesToCheck.filter(isPresent);
    if (cycles.length > 0) {
      const cycleCollectionIdentifiers = cycleCollection.map(cycleItem => this.getCycleIdentifier(cycleItem));
      const cyclesToAdd = cycles.filter(cycleItem => {
        const cycleIdentifier = this.getCycleIdentifier(cycleItem);
        if (cycleCollectionIdentifiers.includes(cycleIdentifier)) {
          return false;
        }
        cycleCollectionIdentifiers.push(cycleIdentifier);
        return true;
      });
      return [...cyclesToAdd, ...cycleCollection];
    }
    return cycleCollection;
  }

  protected convertDateFromClient<T extends ICycle | NewCycle | PartialUpdateCycle>(cycle: T): RestOf<T> {
    return {
      ...cycle,
      dateDebut: cycle.dateDebut?.format(DATE_FORMAT) ?? null,
      dateFin: cycle.dateFin?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restCycle: RestCycle): ICycle {
    return {
      ...restCycle,
      dateDebut: restCycle.dateDebut ? dayjs(restCycle.dateDebut) : undefined,
      dateFin: restCycle.dateFin ? dayjs(restCycle.dateFin) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestCycle>): HttpResponse<ICycle> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestCycle[]>): HttpResponse<ICycle[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
