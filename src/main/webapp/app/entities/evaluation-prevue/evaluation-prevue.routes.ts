import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import EvaluationPrevueResolve from './route/evaluation-prevue-routing-resolve.service';

const evaluationPrevueRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/evaluation-prevue.component').then(m => m.EvaluationPrevueComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/evaluation-prevue-detail.component').then(m => m.EvaluationPrevueDetailComponent),
    resolve: {
      evaluationPrevue: EvaluationPrevueResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/evaluation-prevue-update.component').then(m => m.EvaluationPrevueUpdateComponent),
    resolve: {
      evaluationPrevue: EvaluationPrevueResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/evaluation-prevue-update.component').then(m => m.EvaluationPrevueUpdateComponent),
    resolve: {
      evaluationPrevue: EvaluationPrevueResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default evaluationPrevueRoute;
