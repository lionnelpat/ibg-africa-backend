import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEvenementEtudiant, NewEvenementEtudiant } from '../evenement-etudiant.model';

export type PartialUpdateEvenementEtudiant = Partial<IEvenementEtudiant> & Pick<IEvenementEtudiant, 'id'>;

type RestOf<T extends IEvenementEtudiant | NewEvenementEtudiant> = Omit<T, 'dateEvenement'> & {
  dateEvenement?: string | null;
};

export type RestEvenementEtudiant = RestOf<IEvenementEtudiant>;

export type NewRestEvenementEtudiant = RestOf<NewEvenementEtudiant>;

export type PartialUpdateRestEvenementEtudiant = RestOf<PartialUpdateEvenementEtudiant>;

export type EntityResponseType = HttpResponse<IEvenementEtudiant>;
export type EntityArrayResponseType = HttpResponse<IEvenementEtudiant[]>;

@Injectable({ providedIn: 'root' })
export class EvenementEtudiantService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/evenement-etudiants');

  create(evenementEtudiant: NewEvenementEtudiant): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(evenementEtudiant);
    return this.http
      .post<RestEvenementEtudiant>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(evenementEtudiant: IEvenementEtudiant): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(evenementEtudiant);
    return this.http
      .put<RestEvenementEtudiant>(`${this.resourceUrl}/${this.getEvenementEtudiantIdentifier(evenementEtudiant)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(evenementEtudiant: PartialUpdateEvenementEtudiant): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(evenementEtudiant);
    return this.http
      .patch<RestEvenementEtudiant>(`${this.resourceUrl}/${this.getEvenementEtudiantIdentifier(evenementEtudiant)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestEvenementEtudiant>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestEvenementEtudiant[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getEvenementEtudiantIdentifier(evenementEtudiant: Pick<IEvenementEtudiant, 'id'>): number {
    return evenementEtudiant.id;
  }

  compareEvenementEtudiant(o1: Pick<IEvenementEtudiant, 'id'> | null, o2: Pick<IEvenementEtudiant, 'id'> | null): boolean {
    return o1 && o2 ? this.getEvenementEtudiantIdentifier(o1) === this.getEvenementEtudiantIdentifier(o2) : o1 === o2;
  }

  addEvenementEtudiantToCollectionIfMissing<Type extends Pick<IEvenementEtudiant, 'id'>>(
    evenementEtudiantCollection: Type[],
    ...evenementEtudiantsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const evenementEtudiants: Type[] = evenementEtudiantsToCheck.filter(isPresent);
    if (evenementEtudiants.length > 0) {
      const evenementEtudiantCollectionIdentifiers = evenementEtudiantCollection.map(evenementEtudiantItem =>
        this.getEvenementEtudiantIdentifier(evenementEtudiantItem),
      );
      const evenementEtudiantsToAdd = evenementEtudiants.filter(evenementEtudiantItem => {
        const evenementEtudiantIdentifier = this.getEvenementEtudiantIdentifier(evenementEtudiantItem);
        if (evenementEtudiantCollectionIdentifiers.includes(evenementEtudiantIdentifier)) {
          return false;
        }
        evenementEtudiantCollectionIdentifiers.push(evenementEtudiantIdentifier);
        return true;
      });
      return [...evenementEtudiantsToAdd, ...evenementEtudiantCollection];
    }
    return evenementEtudiantCollection;
  }

  protected convertDateFromClient<T extends IEvenementEtudiant | NewEvenementEtudiant | PartialUpdateEvenementEtudiant>(
    evenementEtudiant: T,
  ): RestOf<T> {
    return {
      ...evenementEtudiant,
      dateEvenement: evenementEtudiant.dateEvenement?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restEvenementEtudiant: RestEvenementEtudiant): IEvenementEtudiant {
    return {
      ...restEvenementEtudiant,
      dateEvenement: restEvenementEtudiant.dateEvenement ? dayjs(restEvenementEtudiant.dateEvenement) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestEvenementEtudiant>): HttpResponse<IEvenementEtudiant> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestEvenementEtudiant[]>): HttpResponse<IEvenementEtudiant[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
