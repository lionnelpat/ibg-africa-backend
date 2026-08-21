import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBaremeMention } from '../bareme-mention.model';
import { BaremeMentionService } from '../service/bareme-mention.service';

const baremeMentionResolve = (route: ActivatedRouteSnapshot): Observable<null | IBaremeMention> => {
  const id = route.params.id;
  if (id) {
    return inject(BaremeMentionService)
      .find(id)
      .pipe(
        mergeMap((baremeMention: HttpResponse<IBaremeMention>) => {
          if (baremeMention.body) {
            return of(baremeMention.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default baremeMentionResolve;
