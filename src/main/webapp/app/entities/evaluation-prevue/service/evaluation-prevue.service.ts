import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEvaluationPrevue, NewEvaluationPrevue } from '../evaluation-prevue.model';

export type PartialUpdateEvaluationPrevue = Partial<IEvaluationPrevue> & Pick<IEvaluationPrevue, 'id'>;

type RestOf<T extends IEvaluationPrevue | NewEvaluationPrevue> = Omit<T, 'dateDebut' | 'dateFin'> & {
  dateDebut?: string | null;
  dateFin?: string | null;
};

export type RestEvaluationPrevue = RestOf<IEvaluationPrevue>;

export type NewRestEvaluationPrevue = RestOf<NewEvaluationPrevue>;

export type PartialUpdateRestEvaluationPrevue = RestOf<PartialUpdateEvaluationPrevue>;

export type EntityResponseType = HttpResponse<IEvaluationPrevue>;
export type EntityArrayResponseType = HttpResponse<IEvaluationPrevue[]>;

@Injectable({ providedIn: 'root' })
export class EvaluationPrevueService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/evaluation-prevues');

  create(evaluationPrevue: NewEvaluationPrevue): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(evaluationPrevue);
    return this.http
      .post<RestEvaluationPrevue>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(evaluationPrevue: IEvaluationPrevue): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(evaluationPrevue);
    return this.http
      .put<RestEvaluationPrevue>(`${this.resourceUrl}/${this.getEvaluationPrevueIdentifier(evaluationPrevue)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(evaluationPrevue: PartialUpdateEvaluationPrevue): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(evaluationPrevue);
    return this.http
      .patch<RestEvaluationPrevue>(`${this.resourceUrl}/${this.getEvaluationPrevueIdentifier(evaluationPrevue)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestEvaluationPrevue>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestEvaluationPrevue[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getEvaluationPrevueIdentifier(evaluationPrevue: Pick<IEvaluationPrevue, 'id'>): number {
    return evaluationPrevue.id;
  }

  compareEvaluationPrevue(o1: Pick<IEvaluationPrevue, 'id'> | null, o2: Pick<IEvaluationPrevue, 'id'> | null): boolean {
    return o1 && o2 ? this.getEvaluationPrevueIdentifier(o1) === this.getEvaluationPrevueIdentifier(o2) : o1 === o2;
  }

  addEvaluationPrevueToCollectionIfMissing<Type extends Pick<IEvaluationPrevue, 'id'>>(
    evaluationPrevueCollection: Type[],
    ...evaluationPrevuesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const evaluationPrevues: Type[] = evaluationPrevuesToCheck.filter(isPresent);
    if (evaluationPrevues.length > 0) {
      const evaluationPrevueCollectionIdentifiers = evaluationPrevueCollection.map(evaluationPrevueItem =>
        this.getEvaluationPrevueIdentifier(evaluationPrevueItem),
      );
      const evaluationPrevuesToAdd = evaluationPrevues.filter(evaluationPrevueItem => {
        const evaluationPrevueIdentifier = this.getEvaluationPrevueIdentifier(evaluationPrevueItem);
        if (evaluationPrevueCollectionIdentifiers.includes(evaluationPrevueIdentifier)) {
          return false;
        }
        evaluationPrevueCollectionIdentifiers.push(evaluationPrevueIdentifier);
        return true;
      });
      return [...evaluationPrevuesToAdd, ...evaluationPrevueCollection];
    }
    return evaluationPrevueCollection;
  }

  protected convertDateFromClient<T extends IEvaluationPrevue | NewEvaluationPrevue | PartialUpdateEvaluationPrevue>(
    evaluationPrevue: T,
  ): RestOf<T> {
    return {
      ...evaluationPrevue,
      dateDebut: evaluationPrevue.dateDebut?.format(DATE_FORMAT) ?? null,
      dateFin: evaluationPrevue.dateFin?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restEvaluationPrevue: RestEvaluationPrevue): IEvaluationPrevue {
    return {
      ...restEvaluationPrevue,
      dateDebut: restEvaluationPrevue.dateDebut ? dayjs(restEvaluationPrevue.dateDebut) : undefined,
      dateFin: restEvaluationPrevue.dateFin ? dayjs(restEvaluationPrevue.dateFin) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestEvaluationPrevue>): HttpResponse<IEvaluationPrevue> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestEvaluationPrevue[]>): HttpResponse<IEvaluationPrevue[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
