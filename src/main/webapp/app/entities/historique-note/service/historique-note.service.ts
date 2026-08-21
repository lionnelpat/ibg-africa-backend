import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IHistoriqueNote } from '../historique-note.model';

type RestOf<T extends IHistoriqueNote> = Omit<T, 'modifieLe'> & {
  modifieLe?: string | null;
};

export type RestHistoriqueNote = RestOf<IHistoriqueNote>;

export type EntityResponseType = HttpResponse<IHistoriqueNote>;
export type EntityArrayResponseType = HttpResponse<IHistoriqueNote[]>;

@Injectable({ providedIn: 'root' })
export class HistoriqueNoteService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/historique-notes');

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestHistoriqueNote>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestHistoriqueNote[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  getHistoriqueNoteIdentifier(historiqueNote: Pick<IHistoriqueNote, 'id'>): number {
    return historiqueNote.id;
  }

  compareHistoriqueNote(o1: Pick<IHistoriqueNote, 'id'> | null, o2: Pick<IHistoriqueNote, 'id'> | null): boolean {
    return o1 && o2 ? this.getHistoriqueNoteIdentifier(o1) === this.getHistoriqueNoteIdentifier(o2) : o1 === o2;
  }

  addHistoriqueNoteToCollectionIfMissing<Type extends Pick<IHistoriqueNote, 'id'>>(
    historiqueNoteCollection: Type[],
    ...historiqueNotesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const historiqueNotes: Type[] = historiqueNotesToCheck.filter(isPresent);
    if (historiqueNotes.length > 0) {
      const historiqueNoteCollectionIdentifiers = historiqueNoteCollection.map(historiqueNoteItem =>
        this.getHistoriqueNoteIdentifier(historiqueNoteItem),
      );
      const historiqueNotesToAdd = historiqueNotes.filter(historiqueNoteItem => {
        const historiqueNoteIdentifier = this.getHistoriqueNoteIdentifier(historiqueNoteItem);
        if (historiqueNoteCollectionIdentifiers.includes(historiqueNoteIdentifier)) {
          return false;
        }
        historiqueNoteCollectionIdentifiers.push(historiqueNoteIdentifier);
        return true;
      });
      return [...historiqueNotesToAdd, ...historiqueNoteCollection];
    }
    return historiqueNoteCollection;
  }

  protected convertDateFromClient<T extends IHistoriqueNote>(historiqueNote: T): RestOf<T> {
    return {
      ...historiqueNote,
      modifieLe: historiqueNote.modifieLe?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restHistoriqueNote: RestHistoriqueNote): IHistoriqueNote {
    return {
      ...restHistoriqueNote,
      modifieLe: restHistoriqueNote.modifieLe ? dayjs(restHistoriqueNote.modifieLe) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestHistoriqueNote>): HttpResponse<IHistoriqueNote> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestHistoriqueNote[]>): HttpResponse<IHistoriqueNote[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
