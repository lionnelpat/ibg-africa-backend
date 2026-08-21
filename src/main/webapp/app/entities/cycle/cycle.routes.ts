import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import CycleResolve from './route/cycle-routing-resolve.service';

const cycleRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/cycle.component').then(m => m.CycleComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/cycle-detail.component').then(m => m.CycleDetailComponent),
    resolve: {
      cycle: CycleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/cycle-update.component').then(m => m.CycleUpdateComponent),
    resolve: {
      cycle: CycleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/cycle-update.component').then(m => m.CycleUpdateComponent),
    resolve: {
      cycle: CycleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default cycleRoute;
