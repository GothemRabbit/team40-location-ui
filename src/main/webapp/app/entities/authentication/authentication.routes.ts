import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import AuthenticationResolve from './route/authentication-routing-resolve.service';

const authenticationRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/authentication.component').then(m => m.AuthenticationComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/authentication-detail.component').then(m => m.AuthenticationDetailComponent),
    resolve: {
      authentication: AuthenticationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/authentication-update.component').then(m => m.AuthenticationUpdateComponent),
    resolve: {
      authentication: AuthenticationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/authentication-update.component').then(m => m.AuthenticationUpdateComponent),
    resolve: {
      authentication: AuthenticationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default authenticationRoute;
