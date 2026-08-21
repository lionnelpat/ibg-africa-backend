import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEvenementEtudiant } from '../evenement-etudiant.model';
import { EvenementEtudiantService } from '../service/evenement-etudiant.service';

const evenementEtudiantResolve = (route: ActivatedRouteSnapshot): Observable<null | IEvenementEtudiant> => {
  const id = route.params.id;
  if (id) {
    return inject(EvenementEtudiantService)
      .find(id)
      .pipe(
        mergeMap((evenementEtudiant: HttpResponse<IEvenementEtudiant>) => {
          if (evenementEtudiant.body) {
            return of(evenementEtudiant.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default evenementEtudiantResolve;
