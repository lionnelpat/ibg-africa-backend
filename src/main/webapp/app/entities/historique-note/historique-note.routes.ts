import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import HistoriqueNoteResolve from './route/historique-note-routing-resolve.service';

const historiqueNoteRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/historique-note.component').then(m => m.HistoriqueNoteComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/historique-note-detail.component').then(m => m.HistoriqueNoteDetailComponent),
    resolve: {
      historiqueNote: HistoriqueNoteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default historiqueNoteRoute;
