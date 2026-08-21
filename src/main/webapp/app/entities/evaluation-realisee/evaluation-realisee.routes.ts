import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import EvaluationRealiseeResolve from './route/evaluation-realisee-routing-resolve.service';

const evaluationRealiseeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/evaluation-realisee.component').then(m => m.EvaluationRealiseeComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/evaluation-realisee-detail.component').then(m => m.EvaluationRealiseeDetailComponent),
    resolve: {
      evaluationRealisee: EvaluationRealiseeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/evaluation-realisee-update.component').then(m => m.EvaluationRealiseeUpdateComponent),
    resolve: {
      evaluationRealisee: EvaluationRealiseeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/evaluation-realisee-update.component').then(m => m.EvaluationRealiseeUpdateComponent),
    resolve: {
      evaluationRealisee: EvaluationRealiseeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default evaluationRealiseeRoute;
