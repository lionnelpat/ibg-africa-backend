import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import InscriptionCycleResolve from './route/inscription-cycle-routing-resolve.service';

const inscriptionCycleRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/inscription-cycle.component').then(m => m.InscriptionCycleComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/inscription-cycle-detail.component').then(m => m.InscriptionCycleDetailComponent),
    resolve: {
      inscriptionCycle: InscriptionCycleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/inscription-cycle-update.component').then(m => m.InscriptionCycleUpdateComponent),
    resolve: {
      inscriptionCycle: InscriptionCycleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/inscription-cycle-update.component').then(m => m.InscriptionCycleUpdateComponent),
    resolve: {
      inscriptionCycle: InscriptionCycleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default inscriptionCycleRoute;
