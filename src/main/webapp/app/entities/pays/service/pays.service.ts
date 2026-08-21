import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPays, NewPays } from '../pays.model';

export type PartialUpdatePays = Partial<IPays> & Pick<IPays, 'id'>;

export type EntityResponseType = HttpResponse<IPays>;
export type EntityArrayResponseType = HttpResponse<IPays[]>;

@Injectable({ providedIn: 'root' })
export class PaysService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/pays');

  create(pays: NewPays): Observable<EntityResponseType> {
    return this.http.post<IPays>(this.resourceUrl, pays, { observe: 'response' });
  }

  update(pays: IPays): Observable<EntityResponseType> {
    return this.http.put<IPays>(`${this.resourceUrl}/${this.getPaysIdentifier(pays)}`, pays, { observe: 'response' });
  }

  partialUpdate(pays: PartialUpdatePays): Observable<EntityResponseType> {
    return this.http.patch<IPays>(`${this.resourceUrl}/${this.getPaysIdentifier(pays)}`, pays, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPays>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPays[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPaysIdentifier(pays: Pick<IPays, 'id'>): number {
    return pays.id;
  }

  comparePays(o1: Pick<IPays, 'id'> | null, o2: Pick<IPays, 'id'> | null): boolean {
    return o1 && o2 ? this.getPaysIdentifier(o1) === this.getPaysIdentifier(o2) : o1 === o2;
  }

  addPaysToCollectionIfMissing<Type extends Pick<IPays, 'id'>>(
    paysCollection: Type[],
    ...paysToCheck: (Type | null | undefined)[]
  ): Type[] {
    const pays: Type[] = paysToCheck.filter(isPresent);
    if (pays.length > 0) {
      const paysCollectionIdentifiers = paysCollection.map(paysItem => this.getPaysIdentifier(paysItem));
      const paysToAdd = pays.filter(paysItem => {
        const paysIdentifier = this.getPaysIdentifier(paysItem);
        if (paysCollectionIdentifiers.includes(paysIdentifier)) {
          return false;
        }
        paysCollectionIdentifiers.push(paysIdentifier);
        return true;
      });
      return [...paysToAdd, ...paysCollection];
    }
    return paysCollection;
  }
}
