import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import EvenementEtudiantResolve from './route/evenement-etudiant-routing-resolve.service';

const evenementEtudiantRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/evenement-etudiant.component').then(m => m.EvenementEtudiantComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/evenement-etudiant-detail.component').then(m => m.EvenementEtudiantDetailComponent),
    resolve: {
      evenementEtudiant: EvenementEtudiantResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/evenement-etudiant-update.component').then(m => m.EvenementEtudiantUpdateComponent),
    resolve: {
      evenementEtudiant: EvenementEtudiantResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/evenement-etudiant-update.component').then(m => m.EvenementEtudiantUpdateComponent),
    resolve: {
      evenementEtudiant: EvenementEtudiantResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default evenementEtudiantRoute;
