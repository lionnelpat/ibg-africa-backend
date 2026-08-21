import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import CoursResolve from './route/cours-routing-resolve.service';

const coursRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/cours.component').then(m => m.CoursComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/cours-detail.component').then(m => m.CoursDetailComponent),
    resolve: {
      cours: CoursResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/cours-update.component').then(m => m.CoursUpdateComponent),
    resolve: {
      cours: CoursResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/cours-update.component').then(m => m.CoursUpdateComponent),
    resolve: {
      cours: CoursResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default coursRoute;
