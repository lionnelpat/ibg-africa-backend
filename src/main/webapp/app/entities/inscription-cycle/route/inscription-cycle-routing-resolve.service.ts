import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInscriptionCycle } from '../inscription-cycle.model';
import { InscriptionCycleService } from '../service/inscription-cycle.service';

const inscriptionCycleResolve = (route: ActivatedRouteSnapshot): Observable<null | IInscriptionCycle> => {
  const id = route.params.id;
  if (id) {
    return inject(InscriptionCycleService)
      .find(id)
      .pipe(
        mergeMap((inscriptionCycle: HttpResponse<IInscriptionCycle>) => {
          if (inscriptionCycle.body) {
            return of(inscriptionCycle.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default inscriptionCycleResolve;
