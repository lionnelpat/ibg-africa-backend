import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import PaysResolve from './route/pays-routing-resolve.service';

const paysRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/pays.component').then(m => m.PaysComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/pays-detail.component').then(m => m.PaysDetailComponent),
    resolve: {
      pays: PaysResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/pays-update.component').then(m => m.PaysUpdateComponent),
    resolve: {
      pays: PaysResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/pays-update.component').then(m => m.PaysUpdateComponent),
    resolve: {
      pays: PaysResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default paysRoute;
