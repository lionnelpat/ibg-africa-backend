import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IHabilitationCycle } from '../habilitation-cycle.model';
import { HabilitationCycleService } from '../service/habilitation-cycle.service';

const habilitationCycleResolve = (route: ActivatedRouteSnapshot): Observable<null | IHabilitationCycle> => {
  const id = route.params.id;
  if (id) {
    return inject(HabilitationCycleService)
      .find(id)
      .pipe(
        mergeMap((habilitationCycle: HttpResponse<IHabilitationCycle>) => {
          if (habilitationCycle.body) {
            return of(habilitationCycle.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default habilitationCycleResolve;
