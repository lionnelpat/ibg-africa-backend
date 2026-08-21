import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICours, NewCours } from '../cours.model';

export type PartialUpdateCours = Partial<ICours> & Pick<ICours, 'id'>;

type RestOf<T extends ICours | NewCours> = Omit<T, 'dateDebut' | 'dateFin'> & {
  dateDebut?: string | null;
  dateFin?: string | null;
};

export type RestCours = RestOf<ICours>;

export type NewRestCours = RestOf<NewCours>;

export type PartialUpdateRestCours = RestOf<PartialUpdateCours>;

export type EntityResponseType = HttpResponse<ICours>;
export type EntityArrayResponseType = HttpResponse<ICours[]>;

@Injectable({ providedIn: 'root' })
export class CoursService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/cours');

  create(cours: NewCours): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cours);
    return this.http.post<RestCours>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(cours: ICours): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cours);
    return this.http
      .put<RestCours>(`${this.resourceUrl}/${this.getCoursIdentifier(cours)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(cours: PartialUpdateCours): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(cours);
    return this.http
      .patch<RestCours>(`${this.resourceUrl}/${this.getCoursIdentifier(cours)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestCours>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestCours[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCoursIdentifier(cours: Pick<ICours, 'id'>): number {
    return cours.id;
  }

  compareCours(o1: Pick<ICours, 'id'> | null, o2: Pick<ICours, 'id'> | null): boolean {
    return o1 && o2 ? this.getCoursIdentifier(o1) === this.getCoursIdentifier(o2) : o1 === o2;
  }

  addCoursToCollectionIfMissing<Type extends Pick<ICours, 'id'>>(
    coursCollection: Type[],
    ...coursToCheck: (Type | null | undefined)[]
  ): Type[] {
    const cours: Type[] = coursToCheck.filter(isPresent);
    if (cours.length > 0) {
      const coursCollectionIdentifiers = coursCollection.map(coursItem => this.getCoursIdentifier(coursItem));
      const coursToAdd = cours.filter(coursItem => {
        const coursIdentifier = this.getCoursIdentifier(coursItem);
        if (coursCollectionIdentifiers.includes(coursIdentifier)) {
          return false;
        }
        coursCollectionIdentifiers.push(coursIdentifier);
        return true;
      });
      return [...coursToAdd, ...coursCollection];
    }
    return coursCollection;
  }

  protected convertDateFromClient<T extends ICours | NewCours | PartialUpdateCours>(cours: T): RestOf<T> {
    return {
      ...cours,
      dateDebut: cours.dateDebut?.format(DATE_FORMAT) ?? null,
      dateFin: cours.dateFin?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restCours: RestCours): ICours {
    return {
      ...restCours,
      dateDebut: restCours.dateDebut ? dayjs(restCours.dateDebut) : undefined,
      dateFin: restCours.dateFin ? dayjs(restCours.dateFin) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestCours>): HttpResponse<ICours> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestCours[]>): HttpResponse<ICours[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
