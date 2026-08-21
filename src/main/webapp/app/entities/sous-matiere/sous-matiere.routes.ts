import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import SousMatiereResolve from './route/sous-matiere-routing-resolve.service';

const sousMatiereRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/sous-matiere.component').then(m => m.SousMatiereComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/sous-matiere-detail.component').then(m => m.SousMatiereDetailComponent),
    resolve: {
      sousMatiere: SousMatiereResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/sous-matiere-update.component').then(m => m.SousMatiereUpdateComponent),
    resolve: {
      sousMatiere: SousMatiereResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/sous-matiere-update.component').then(m => m.SousMatiereUpdateComponent),
    resolve: {
      sousMatiere: SousMatiereResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default sousMatiereRoute;
