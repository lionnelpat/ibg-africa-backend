import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITypeTache } from '../type-tache.model';
import { TypeTacheService } from '../service/type-tache.service';

const typeTacheResolve = (route: ActivatedRouteSnapshot): Observable<null | ITypeTache> => {
  const id = route.params.id;
  if (id) {
    return inject(TypeTacheService)
      .find(id)
      .pipe(
        mergeMap((typeTache: HttpResponse<ITypeTache>) => {
          if (typeTache.body) {
            return of(typeTache.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default typeTacheResolve;
