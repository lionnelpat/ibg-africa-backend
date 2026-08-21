import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEvaluationRealisee } from '../evaluation-realisee.model';
import { EvaluationRealiseeService } from '../service/evaluation-realisee.service';

const evaluationRealiseeResolve = (route: ActivatedRouteSnapshot): Observable<null | IEvaluationRealisee> => {
  const id = route.params.id;
  if (id) {
    return inject(EvaluationRealiseeService)
      .find(id)
      .pipe(
        mergeMap((evaluationRealisee: HttpResponse<IEvaluationRealisee>) => {
          if (evaluationRealisee.body) {
            return of(evaluationRealisee.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default evaluationRealiseeResolve;
