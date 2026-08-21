import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IParametre, NewParametre } from '../parametre.model';

export type PartialUpdateParametre = Partial<IParametre> & Pick<IParametre, 'id'>;

export type EntityResponseType = HttpResponse<IParametre>;
export type EntityArrayResponseType = HttpResponse<IParametre[]>;

@Injectable({ providedIn: 'root' })
export class ParametreService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/parametres');

  create(parametre: NewParametre): Observable<EntityResponseType> {
    return this.http.post<IParametre>(this.resourceUrl, parametre, { observe: 'response' });
  }

  update(parametre: IParametre): Observable<EntityResponseType> {
    return this.http.put<IParametre>(`${this.resourceUrl}/${this.getParametreIdentifier(parametre)}`, parametre, { observe: 'response' });
  }

  partialUpdate(parametre: PartialUpdateParametre): Observable<EntityResponseType> {
    return this.http.patch<IParametre>(`${this.resourceUrl}/${this.getParametreIdentifier(parametre)}`, parametre, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IParametre>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IParametre[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getParametreIdentifier(parametre: Pick<IParametre, 'id'>): number {
    return parametre.id;
  }

  compareParametre(o1: Pick<IParametre, 'id'> | null, o2: Pick<IParametre, 'id'> | null): boolean {
    return o1 && o2 ? this.getParametreIdentifier(o1) === this.getParametreIdentifier(o2) : o1 === o2;
  }

  addParametreToCollectionIfMissing<Type extends Pick<IParametre, 'id'>>(
    parametreCollection: Type[],
    ...parametresToCheck: (Type | null | undefined)[]
  ): Type[] {
    const parametres: Type[] = parametresToCheck.filter(isPresent);
    if (parametres.length > 0) {
      const parametreCollectionIdentifiers = parametreCollection.map(parametreItem => this.getParametreIdentifier(parametreItem));
      const parametresToAdd = parametres.filter(parametreItem => {
        const parametreIdentifier = this.getParametreIdentifier(parametreItem);
        if (parametreCollectionIdentifiers.includes(parametreIdentifier)) {
          return false;
        }
        parametreCollectionIdentifiers.push(parametreIdentifier);
        return true;
      });
      return [...parametresToAdd, ...parametreCollection];
    }
    return parametreCollection;
  }
}
