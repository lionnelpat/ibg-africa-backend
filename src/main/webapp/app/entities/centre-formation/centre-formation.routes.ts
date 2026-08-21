import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import CentreFormationResolve from './route/centre-formation-routing-resolve.service';

const centreFormationRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/centre-formation.component').then(m => m.CentreFormationComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/centre-formation-detail.component').then(m => m.CentreFormationDetailComponent),
    resolve: {
      centreFormation: CentreFormationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/centre-formation-update.component').then(m => m.CentreFormationUpdateComponent),
    resolve: {
      centreFormation: CentreFormationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/centre-formation-update.component').then(m => m.CentreFormationUpdateComponent),
    resolve: {
      centreFormation: CentreFormationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default centreFormationRoute;
