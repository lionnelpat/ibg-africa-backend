import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEnseignant, NewEnseignant } from '../enseignant.model';

export type PartialUpdateEnseignant = Partial<IEnseignant> & Pick<IEnseignant, 'id'>;

export type EntityResponseType = HttpResponse<IEnseignant>;
export type EntityArrayResponseType = HttpResponse<IEnseignant[]>;

@Injectable({ providedIn: 'root' })
export class EnseignantService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/enseignants');

  create(enseignant: NewEnseignant): Observable<EntityResponseType> {
    return this.http.post<IEnseignant>(this.resourceUrl, enseignant, { observe: 'response' });
  }

  update(enseignant: IEnseignant): Observable<EntityResponseType> {
    return this.http.put<IEnseignant>(`${this.resourceUrl}/${this.getEnseignantIdentifier(enseignant)}`, enseignant, {
      observe: 'response',
    });
  }

  partialUpdate(enseignant: PartialUpdateEnseignant): Observable<EntityResponseType> {
    return this.http.patch<IEnseignant>(`${this.resourceUrl}/${this.getEnseignantIdentifier(enseignant)}`, enseignant, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEnseignant>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEnseignant[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getEnseignantIdentifier(enseignant: Pick<IEnseignant, 'id'>): number {
    return enseignant.id;
  }

  compareEnseignant(o1: Pick<IEnseignant, 'id'> | null, o2: Pick<IEnseignant, 'id'> | null): boolean {
    return o1 && o2 ? this.getEnseignantIdentifier(o1) === this.getEnseignantIdentifier(o2) : o1 === o2;
  }

  addEnseignantToCollectionIfMissing<Type extends Pick<IEnseignant, 'id'>>(
    enseignantCollection: Type[],
    ...enseignantsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const enseignants: Type[] = enseignantsToCheck.filter(isPresent);
    if (enseignants.length > 0) {
      const enseignantCollectionIdentifiers = enseignantCollection.map(enseignantItem => this.getEnseignantIdentifier(enseignantItem));
      const enseignantsToAdd = enseignants.filter(enseignantItem => {
        const enseignantIdentifier = this.getEnseignantIdentifier(enseignantItem);
        if (enseignantCollectionIdentifiers.includes(enseignantIdentifier)) {
          return false;
        }
        enseignantCollectionIdentifiers.push(enseignantIdentifier);
        return true;
      });
      return [...enseignantsToAdd, ...enseignantCollection];
    }
    return enseignantCollection;
  }
}
