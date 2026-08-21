import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IHistoriqueNote } from '../historique-note.model';
import { HistoriqueNoteService } from '../service/historique-note.service';

const historiqueNoteResolve = (route: ActivatedRouteSnapshot): Observable<null | IHistoriqueNote> => {
  const id = route.params.id;
  if (id) {
    return inject(HistoriqueNoteService)
      .find(id)
      .pipe(
        mergeMap((historiqueNote: HttpResponse<IHistoriqueNote>) => {
          if (historiqueNote.body) {
            return of(historiqueNote.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default historiqueNoteResolve;
