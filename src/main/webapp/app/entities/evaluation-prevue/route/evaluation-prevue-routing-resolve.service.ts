import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEvaluationPrevue } from '../evaluation-prevue.model';
import { EvaluationPrevueService } from '../service/evaluation-prevue.service';

const evaluationPrevueResolve = (route: ActivatedRouteSnapshot): Observable<null | IEvaluationPrevue> => {
  const id = route.params.id;
  if (id) {
    return inject(EvaluationPrevueService)
      .find(id)
      .pipe(
        mergeMap((evaluationPrevue: HttpResponse<IEvaluationPrevue>) => {
          if (evaluationPrevue.body) {
            return of(evaluationPrevue.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default evaluationPrevueResolve;
