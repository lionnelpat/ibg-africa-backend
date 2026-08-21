import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import TypeTacheResolve from './route/type-tache-routing-resolve.service';

const typeTacheRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/type-tache.component').then(m => m.TypeTacheComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/type-tache-detail.component').then(m => m.TypeTacheDetailComponent),
    resolve: {
      typeTache: TypeTacheResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/type-tache-update.component').then(m => m.TypeTacheUpdateComponent),
    resolve: {
      typeTache: TypeTacheResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/type-tache-update.component').then(m => m.TypeTacheUpdateComponent),
    resolve: {
      typeTache: TypeTacheResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default typeTacheRoute;
