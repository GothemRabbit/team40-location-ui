import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import LikesResolve from './route/likes-routing-resolve.service';

const likesRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/likes.component').then(m => m.LikesComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/likes-detail.component').then(m => m.LikesDetailComponent),
    resolve: {
      likes: LikesResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/likes-update.component').then(m => m.LikesUpdateComponent),
    resolve: {
      likes: LikesResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/likes-update.component').then(m => m.LikesUpdateComponent),
    resolve: {
      likes: LikesResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default likesRoute;
