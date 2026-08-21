import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITypeTache, NewTypeTache } from '../type-tache.model';

export type PartialUpdateTypeTache = Partial<ITypeTache> & Pick<ITypeTache, 'id'>;

export type EntityResponseType = HttpResponse<ITypeTache>;
export type EntityArrayResponseType = HttpResponse<ITypeTache[]>;

@Injectable({ providedIn: 'root' })
export class TypeTacheService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/type-taches');

  create(typeTache: NewTypeTache): Observable<EntityResponseType> {
    return this.http.post<ITypeTache>(this.resourceUrl, typeTache, { observe: 'response' });
  }

  update(typeTache: ITypeTache): Observable<EntityResponseType> {
    return this.http.put<ITypeTache>(`${this.resourceUrl}/${this.getTypeTacheIdentifier(typeTache)}`, typeTache, { observe: 'response' });
  }

  partialUpdate(typeTache: PartialUpdateTypeTache): Observable<EntityResponseType> {
    return this.http.patch<ITypeTache>(`${this.resourceUrl}/${this.getTypeTacheIdentifier(typeTache)}`, typeTache, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITypeTache>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITypeTache[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTypeTacheIdentifier(typeTache: Pick<ITypeTache, 'id'>): number {
    return typeTache.id;
  }

  compareTypeTache(o1: Pick<ITypeTache, 'id'> | null, o2: Pick<ITypeTache, 'id'> | null): boolean {
    return o1 && o2 ? this.getTypeTacheIdentifier(o1) === this.getTypeTacheIdentifier(o2) : o1 === o2;
  }

  addTypeTacheToCollectionIfMissing<Type extends Pick<ITypeTache, 'id'>>(
    typeTacheCollection: Type[],
    ...typeTachesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const typeTaches: Type[] = typeTachesToCheck.filter(isPresent);
    if (typeTaches.length > 0) {
      const typeTacheCollectionIdentifiers = typeTacheCollection.map(typeTacheItem => this.getTypeTacheIdentifier(typeTacheItem));
      const typeTachesToAdd = typeTaches.filter(typeTacheItem => {
        const typeTacheIdentifier = this.getTypeTacheIdentifier(typeTacheItem);
        if (typeTacheCollectionIdentifiers.includes(typeTacheIdentifier)) {
          return false;
        }
        typeTacheCollectionIdentifiers.push(typeTacheIdentifier);
        return true;
      });
      return [...typeTachesToAdd, ...typeTacheCollection];
    }
    return typeTacheCollection;
  }
}
