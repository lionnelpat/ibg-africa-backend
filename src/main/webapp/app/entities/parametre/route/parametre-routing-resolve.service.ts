import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IParametre } from '../parametre.model';
import { ParametreService } from '../service/parametre.service';

const parametreResolve = (route: ActivatedRouteSnapshot): Observable<null | IParametre> => {
  const id = route.params.id;
  if (id) {
    return inject(ParametreService)
      .find(id)
      .pipe(
        mergeMap((parametre: HttpResponse<IParametre>) => {
          if (parametre.body) {
            return of(parametre.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default parametreResolve;
