import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import HabilitationCycleResolve from './route/habilitation-cycle-routing-resolve.service';

const habilitationCycleRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/habilitation-cycle.component').then(m => m.HabilitationCycleComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/habilitation-cycle-detail.component').then(m => m.HabilitationCycleDetailComponent),
    resolve: {
      habilitationCycle: HabilitationCycleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/habilitation-cycle-update.component').then(m => m.HabilitationCycleUpdateComponent),
    resolve: {
      habilitationCycle: HabilitationCycleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/habilitation-cycle-update.component').then(m => m.HabilitationCycleUpdateComponent),
    resolve: {
      habilitationCycle: HabilitationCycleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default habilitationCycleRoute;
