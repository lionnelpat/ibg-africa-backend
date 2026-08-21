import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IInscriptionCycle, NewInscriptionCycle } from '../inscription-cycle.model';

export type PartialUpdateInscriptionCycle = Partial<IInscriptionCycle> & Pick<IInscriptionCycle, 'id'>;

type RestOf<T extends IInscriptionCycle | NewInscriptionCycle> = Omit<T, 'dateInscription'> & {
  dateInscription?: string | null;
};

export type RestInscriptionCycle = RestOf<IInscriptionCycle>;

export type NewRestInscriptionCycle = RestOf<NewInscriptionCycle>;

export type PartialUpdateRestInscriptionCycle = RestOf<PartialUpdateInscriptionCycle>;

export type EntityResponseType = HttpResponse<IInscriptionCycle>;
export type EntityArrayResponseType = HttpResponse<IInscriptionCycle[]>;

@Injectable({ providedIn: 'root' })
export class InscriptionCycleService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/inscription-cycles');

  create(inscriptionCycle: NewInscriptionCycle): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(inscriptionCycle);
    return this.http
      .post<RestInscriptionCycle>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(inscriptionCycle: IInscriptionCycle): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(inscriptionCycle);
    return this.http
      .put<RestInscriptionCycle>(`${this.resourceUrl}/${this.getInscriptionCycleIdentifier(inscriptionCycle)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(inscriptionCycle: PartialUpdateInscriptionCycle): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(inscriptionCycle);
    return this.http
      .patch<RestInscriptionCycle>(`${this.resourceUrl}/${this.getInscriptionCycleIdentifier(inscriptionCycle)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestInscriptionCycle>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestInscriptionCycle[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getInscriptionCycleIdentifier(inscriptionCycle: Pick<IInscriptionCycle, 'id'>): number {
    return inscriptionCycle.id;
  }

  compareInscriptionCycle(o1: Pick<IInscriptionCycle, 'id'> | null, o2: Pick<IInscriptionCycle, 'id'> | null): boolean {
    return o1 && o2 ? this.getInscriptionCycleIdentifier(o1) === this.getInscriptionCycleIdentifier(o2) : o1 === o2;
  }

  addInscriptionCycleToCollectionIfMissing<Type extends Pick<IInscriptionCycle, 'id'>>(
    inscriptionCycleCollection: Type[],
    ...inscriptionCyclesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const inscriptionCycles: Type[] = inscriptionCyclesToCheck.filter(isPresent);
    if (inscriptionCycles.length > 0) {
      const inscriptionCycleCollectionIdentifiers = inscriptionCycleCollection.map(inscriptionCycleItem =>
        this.getInscriptionCycleIdentifier(inscriptionCycleItem),
      );
      const inscriptionCyclesToAdd = inscriptionCycles.filter(inscriptionCycleItem => {
        const inscriptionCycleIdentifier = this.getInscriptionCycleIdentifier(inscriptionCycleItem);
        if (inscriptionCycleCollectionIdentifiers.includes(inscriptionCycleIdentifier)) {
          return false;
        }
        inscriptionCycleCollectionIdentifiers.push(inscriptionCycleIdentifier);
        return true;
      });
      return [...inscriptionCyclesToAdd, ...inscriptionCycleCollection];
    }
    return inscriptionCycleCollection;
  }

  protected convertDateFromClient<T extends IInscriptionCycle | NewInscriptionCycle | PartialUpdateInscriptionCycle>(
    inscriptionCycle: T,
  ): RestOf<T> {
    return {
      ...inscriptionCycle,
      dateInscription: inscriptionCycle.dateInscription?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restInscriptionCycle: RestInscriptionCycle): IInscriptionCycle {
    return {
      ...restInscriptionCycle,
      dateInscription: restInscriptionCycle.dateInscription ? dayjs(restInscriptionCycle.dateInscription) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestInscriptionCycle>): HttpResponse<IInscriptionCycle> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestInscriptionCycle[]>): HttpResponse<IInscriptionCycle[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
