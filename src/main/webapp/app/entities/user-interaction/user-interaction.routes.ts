import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import UserInteractionResolve from './route/user-interaction-routing-resolve.service';

const userInteractionRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/user-interaction.component').then(m => m.UserInteractionComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/user-interaction-detail.component').then(m => m.UserInteractionDetailComponent),
    resolve: {
      userInteraction: UserInteractionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/user-interaction-update.component').then(m => m.UserInteractionUpdateComponent),
    resolve: {
      userInteraction: UserInteractionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/user-interaction-update.component').then(m => m.UserInteractionUpdateComponent),
    resolve: {
      userInteraction: UserInteractionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default userInteractionRoute;
