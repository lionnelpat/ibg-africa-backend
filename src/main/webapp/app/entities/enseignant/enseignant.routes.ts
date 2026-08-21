import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import EnseignantResolve from './route/enseignant-routing-resolve.service';

const enseignantRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/enseignant.component').then(m => m.EnseignantComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/enseignant-detail.component').then(m => m.EnseignantDetailComponent),
    resolve: {
      enseignant: EnseignantResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/enseignant-update.component').then(m => m.EnseignantUpdateComponent),
    resolve: {
      enseignant: EnseignantResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/enseignant-update.component').then(m => m.EnseignantUpdateComponent),
    resolve: {
      enseignant: EnseignantResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default enseignantRoute;
