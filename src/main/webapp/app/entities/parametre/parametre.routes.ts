import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ParametreResolve from './route/parametre-routing-resolve.service';

const parametreRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/parametre.component').then(m => m.ParametreComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/parametre-detail.component').then(m => m.ParametreDetailComponent),
    resolve: {
      parametre: ParametreResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/parametre-update.component').then(m => m.ParametreUpdateComponent),
    resolve: {
      parametre: ParametreResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/parametre-update.component').then(m => m.ParametreUpdateComponent),
    resolve: {
      parametre: ParametreResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default parametreRoute;
