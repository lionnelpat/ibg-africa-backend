import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISousMatiere } from '../sous-matiere.model';
import { SousMatiereService } from '../service/sous-matiere.service';

const sousMatiereResolve = (route: ActivatedRouteSnapshot): Observable<null | ISousMatiere> => {
  const id = route.params.id;
  if (id) {
    return inject(SousMatiereService)
      .find(id)
      .pipe(
        mergeMap((sousMatiere: HttpResponse<ISousMatiere>) => {
          if (sousMatiere.body) {
            return of(sousMatiere.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default sousMatiereResolve;
