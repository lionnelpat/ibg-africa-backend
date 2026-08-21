import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBaremeMention, NewBaremeMention } from '../bareme-mention.model';

export type PartialUpdateBaremeMention = Partial<IBaremeMention> & Pick<IBaremeMention, 'id'>;

export type EntityResponseType = HttpResponse<IBaremeMention>;
export type EntityArrayResponseType = HttpResponse<IBaremeMention[]>;

@Injectable({ providedIn: 'root' })
export class BaremeMentionService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/bareme-mentions');

  create(baremeMention: NewBaremeMention): Observable<EntityResponseType> {
    return this.http.post<IBaremeMention>(this.resourceUrl, baremeMention, { observe: 'response' });
  }

  update(baremeMention: IBaremeMention): Observable<EntityResponseType> {
    return this.http.put<IBaremeMention>(`${this.resourceUrl}/${this.getBaremeMentionIdentifier(baremeMention)}`, baremeMention, {
      observe: 'response',
    });
  }

  partialUpdate(baremeMention: PartialUpdateBaremeMention): Observable<EntityResponseType> {
    return this.http.patch<IBaremeMention>(`${this.resourceUrl}/${this.getBaremeMentionIdentifier(baremeMention)}`, baremeMention, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBaremeMention>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBaremeMention[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getBaremeMentionIdentifier(baremeMention: Pick<IBaremeMention, 'id'>): number {
    return baremeMention.id;
  }

  compareBaremeMention(o1: Pick<IBaremeMention, 'id'> | null, o2: Pick<IBaremeMention, 'id'> | null): boolean {
    return o1 && o2 ? this.getBaremeMentionIdentifier(o1) === this.getBaremeMentionIdentifier(o2) : o1 === o2;
  }

  addBaremeMentionToCollectionIfMissing<Type extends Pick<IBaremeMention, 'id'>>(
    baremeMentionCollection: Type[],
    ...baremeMentionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const baremeMentions: Type[] = baremeMentionsToCheck.filter(isPresent);
    if (baremeMentions.length > 0) {
      const baremeMentionCollectionIdentifiers = baremeMentionCollection.map(baremeMentionItem =>
        this.getBaremeMentionIdentifier(baremeMentionItem),
      );
      const baremeMentionsToAdd = baremeMentions.filter(baremeMentionItem => {
        const baremeMentionIdentifier = this.getBaremeMentionIdentifier(baremeMentionItem);
        if (baremeMentionCollectionIdentifiers.includes(baremeMentionIdentifier)) {
          return false;
        }
        baremeMentionCollectionIdentifiers.push(baremeMentionIdentifier);
        return true;
      });
      return [...baremeMentionsToAdd, ...baremeMentionCollection];
    }
    return baremeMentionCollection;
  }
}
