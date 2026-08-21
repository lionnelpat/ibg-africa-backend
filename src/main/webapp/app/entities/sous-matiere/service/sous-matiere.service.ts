import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISousMatiere, NewSousMatiere } from '../sous-matiere.model';

export type PartialUpdateSousMatiere = Partial<ISousMatiere> & Pick<ISousMatiere, 'id'>;

export type EntityResponseType = HttpResponse<ISousMatiere>;
export type EntityArrayResponseType = HttpResponse<ISousMatiere[]>;

@Injectable({ providedIn: 'root' })
export class SousMatiereService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/sous-matieres');

  create(sousMatiere: NewSousMatiere): Observable<EntityResponseType> {
    return this.http.post<ISousMatiere>(this.resourceUrl, sousMatiere, { observe: 'response' });
  }

  update(sousMatiere: ISousMatiere): Observable<EntityResponseType> {
    return this.http.put<ISousMatiere>(`${this.resourceUrl}/${this.getSousMatiereIdentifier(sousMatiere)}`, sousMatiere, {
      observe: 'response',
    });
  }

  partialUpdate(sousMatiere: PartialUpdateSousMatiere): Observable<EntityResponseType> {
    return this.http.patch<ISousMatiere>(`${this.resourceUrl}/${this.getSousMatiereIdentifier(sousMatiere)}`, sousMatiere, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISousMatiere>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISousMatiere[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSousMatiereIdentifier(sousMatiere: Pick<ISousMatiere, 'id'>): number {
    return sousMatiere.id;
  }

  compareSousMatiere(o1: Pick<ISousMatiere, 'id'> | null, o2: Pick<ISousMatiere, 'id'> | null): boolean {
    return o1 && o2 ? this.getSousMatiereIdentifier(o1) === this.getSousMatiereIdentifier(o2) : o1 === o2;
  }

  addSousMatiereToCollectionIfMissing<Type extends Pick<ISousMatiere, 'id'>>(
    sousMatiereCollection: Type[],
    ...sousMatieresToCheck: (Type | null | undefined)[]
  ): Type[] {
    const sousMatieres: Type[] = sousMatieresToCheck.filter(isPresent);
    if (sousMatieres.length > 0) {
      const sousMatiereCollectionIdentifiers = sousMatiereCollection.map(sousMatiereItem => this.getSousMatiereIdentifier(sousMatiereItem));
      const sousMatieresToAdd = sousMatieres.filter(sousMatiereItem => {
        const sousMatiereIdentifier = this.getSousMatiereIdentifier(sousMatiereItem);
        if (sousMatiereCollectionIdentifiers.includes(sousMatiereIdentifier)) {
          return false;
        }
        sousMatiereCollectionIdentifiers.push(sousMatiereIdentifier);
        return true;
      });
      return [...sousMatieresToAdd, ...sousMatiereCollection];
    }
    return sousMatiereCollection;
  }
}
