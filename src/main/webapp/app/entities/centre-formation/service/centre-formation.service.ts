import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICentreFormation, NewCentreFormation } from '../centre-formation.model';

export type PartialUpdateCentreFormation = Partial<ICentreFormation> & Pick<ICentreFormation, 'id'>;

export type EntityResponseType = HttpResponse<ICentreFormation>;
export type EntityArrayResponseType = HttpResponse<ICentreFormation[]>;

@Injectable({ providedIn: 'root' })
export class CentreFormationService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/centre-formations');

  create(centreFormation: NewCentreFormation): Observable<EntityResponseType> {
    return this.http.post<ICentreFormation>(this.resourceUrl, centreFormation, { observe: 'response' });
  }

  update(centreFormation: ICentreFormation): Observable<EntityResponseType> {
    return this.http.put<ICentreFormation>(`${this.resourceUrl}/${this.getCentreFormationIdentifier(centreFormation)}`, centreFormation, {
      observe: 'response',
    });
  }

  partialUpdate(centreFormation: PartialUpdateCentreFormation): Observable<EntityResponseType> {
    return this.http.patch<ICentreFormation>(`${this.resourceUrl}/${this.getCentreFormationIdentifier(centreFormation)}`, centreFormation, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICentreFormation>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICentreFormation[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCentreFormationIdentifier(centreFormation: Pick<ICentreFormation, 'id'>): number {
    return centreFormation.id;
  }

  compareCentreFormation(o1: Pick<ICentreFormation, 'id'> | null, o2: Pick<ICentreFormation, 'id'> | null): boolean {
    return o1 && o2 ? this.getCentreFormationIdentifier(o1) === this.getCentreFormationIdentifier(o2) : o1 === o2;
  }

  addCentreFormationToCollectionIfMissing<Type extends Pick<ICentreFormation, 'id'>>(
    centreFormationCollection: Type[],
    ...centreFormationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const centreFormations: Type[] = centreFormationsToCheck.filter(isPresent);
    if (centreFormations.length > 0) {
      const centreFormationCollectionIdentifiers = centreFormationCollection.map(centreFormationItem =>
        this.getCentreFormationIdentifier(centreFormationItem),
      );
      const centreFormationsToAdd = centreFormations.filter(centreFormationItem => {
        const centreFormationIdentifier = this.getCentreFormationIdentifier(centreFormationItem);
        if (centreFormationCollectionIdentifiers.includes(centreFormationIdentifier)) {
          return false;
        }
        centreFormationCollectionIdentifiers.push(centreFormationIdentifier);
        return true;
      });
      return [...centreFormationsToAdd, ...centreFormationCollection];
    }
    return centreFormationCollection;
  }
}
