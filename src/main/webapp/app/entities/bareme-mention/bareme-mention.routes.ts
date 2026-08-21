import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import BaremeMentionResolve from './route/bareme-mention-routing-resolve.service';

const baremeMentionRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/bareme-mention.component').then(m => m.BaremeMentionComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/bareme-mention-detail.component').then(m => m.BaremeMentionDetailComponent),
    resolve: {
      baremeMention: BaremeMentionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/bareme-mention-update.component').then(m => m.BaremeMentionUpdateComponent),
    resolve: {
      baremeMention: BaremeMentionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/bareme-mention-update.component').then(m => m.BaremeMentionUpdateComponent),
    resolve: {
      baremeMention: BaremeMentionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default baremeMentionRoute;
