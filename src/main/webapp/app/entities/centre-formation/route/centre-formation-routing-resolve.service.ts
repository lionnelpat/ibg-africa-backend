import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICentreFormation } from '../centre-formation.model';
import { CentreFormationService } from '../service/centre-formation.service';

const centreFormationResolve = (route: ActivatedRouteSnapshot): Observable<null | ICentreFormation> => {
  const id = route.params.id;
  if (id) {
    return inject(CentreFormationService)
      .find(id)
      .pipe(
        mergeMap((centreFormation: HttpResponse<ICentreFormation>) => {
          if (centreFormation.body) {
            return of(centreFormation.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default centreFormationResolve;
