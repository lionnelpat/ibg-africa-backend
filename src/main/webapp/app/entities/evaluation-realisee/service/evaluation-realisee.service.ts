import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEvaluationRealisee, NewEvaluationRealisee } from '../evaluation-realisee.model';

export type PartialUpdateEvaluationRealisee = Partial<IEvaluationRealisee> & Pick<IEvaluationRealisee, 'id'>;

type RestOf<T extends IEvaluationRealisee | NewEvaluationRealisee> = Omit<T, 'dateDebut' | 'dateFin' | 'saisieLe' | 'valideeLe'> & {
  dateDebut?: string | null;
  dateFin?: string | null;
  saisieLe?: string | null;
  valideeLe?: string | null;
};

export type RestEvaluationRealisee = RestOf<IEvaluationRealisee>;

export type NewRestEvaluationRealisee = RestOf<NewEvaluationRealisee>;

export type PartialUpdateRestEvaluationRealisee = RestOf<PartialUpdateEvaluationRealisee>;

export type EntityResponseType = HttpResponse<IEvaluationRealisee>;
export type EntityArrayResponseType = HttpResponse<IEvaluationRealisee[]>;

@Injectable({ providedIn: 'root' })
export class EvaluationRealiseeService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/evaluation-realisees');

  create(evaluationRealisee: NewEvaluationRealisee): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(evaluationRealisee);
    return this.http
      .post<RestEvaluationRealisee>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(evaluationRealisee: IEvaluationRealisee): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(evaluationRealisee);
    return this.http
      .put<RestEvaluationRealisee>(`${this.resourceUrl}/${this.getEvaluationRealiseeIdentifier(evaluationRealisee)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(evaluationRealisee: PartialUpdateEvaluationRealisee): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(evaluationRealisee);
    return this.http
      .patch<RestEvaluationRealisee>(`${this.resourceUrl}/${this.getEvaluationRealiseeIdentifier(evaluationRealisee)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestEvaluationRealisee>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestEvaluationRealisee[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getEvaluationRealiseeIdentifier(evaluationRealisee: Pick<IEvaluationRealisee, 'id'>): number {
    return evaluationRealisee.id;
  }

  compareEvaluationRealisee(o1: Pick<IEvaluationRealisee, 'id'> | null, o2: Pick<IEvaluationRealisee, 'id'> | null): boolean {
    return o1 && o2 ? this.getEvaluationRealiseeIdentifier(o1) === this.getEvaluationRealiseeIdentifier(o2) : o1 === o2;
  }

  addEvaluationRealiseeToCollectionIfMissing<Type extends Pick<IEvaluationRealisee, 'id'>>(
    evaluationRealiseeCollection: Type[],
    ...evaluationRealiseesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const evaluationRealisees: Type[] = evaluationRealiseesToCheck.filter(isPresent);
    if (evaluationRealisees.length > 0) {
      const evaluationRealiseeCollectionIdentifiers = evaluationRealiseeCollection.map(evaluationRealiseeItem =>
        this.getEvaluationRealiseeIdentifier(evaluationRealiseeItem),
      );
      const evaluationRealiseesToAdd = evaluationRealisees.filter(evaluationRealiseeItem => {
        const evaluationRealiseeIdentifier = this.getEvaluationRealiseeIdentifier(evaluationRealiseeItem);
        if (evaluationRealiseeCollectionIdentifiers.includes(evaluationRealiseeIdentifier)) {
          return false;
        }
        evaluationRealiseeCollectionIdentifiers.push(evaluationRealiseeIdentifier);
        return true;
      });
      return [...evaluationRealiseesToAdd, ...evaluationRealiseeCollection];
    }
    return evaluationRealiseeCollection;
  }

  protected convertDateFromClient<T extends IEvaluationRealisee | NewEvaluationRealisee | PartialUpdateEvaluationRealisee>(
    evaluationRealisee: T,
  ): RestOf<T> {
    return {
      ...evaluationRealisee,
      dateDebut: evaluationRealisee.dateDebut?.format(DATE_FORMAT) ?? null,
      dateFin: evaluationRealisee.dateFin?.format(DATE_FORMAT) ?? null,
      saisieLe: evaluationRealisee.saisieLe?.toJSON() ?? null,
      valideeLe: evaluationRealisee.valideeLe?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restEvaluationRealisee: RestEvaluationRealisee): IEvaluationRealisee {
    return {
      ...restEvaluationRealisee,
      dateDebut: restEvaluationRealisee.dateDebut ? dayjs(restEvaluationRealisee.dateDebut) : undefined,
      dateFin: restEvaluationRealisee.dateFin ? dayjs(restEvaluationRealisee.dateFin) : undefined,
      saisieLe: restEvaluationRealisee.saisieLe ? dayjs(restEvaluationRealisee.saisieLe) : undefined,
      valideeLe: restEvaluationRealisee.valideeLe ? dayjs(restEvaluationRealisee.valideeLe) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestEvaluationRealisee>): HttpResponse<IEvaluationRealisee> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestEvaluationRealisee[]>): HttpResponse<IEvaluationRealisee[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
