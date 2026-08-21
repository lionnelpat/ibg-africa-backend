import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPays } from '../pays.model';
import { PaysService } from '../service/pays.service';

const paysResolve = (route: ActivatedRouteSnapshot): Observable<null | IPays> => {
  const id = route.params.id;
  if (id) {
    return inject(PaysService)
      .find(id)
      .pipe(
        mergeMap((pays: HttpResponse<IPays>) => {
          if (pays.body) {
            return of(pays.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default paysResolve;
