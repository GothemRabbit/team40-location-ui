import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import UserDetailsResolve from './route/user-details-routing-resolve.service';

const userDetailsRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/user-details.component').then(m => m.UserDetailsComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/user-details-detail.component').then(m => m.UserDetailsDetailComponent),
    resolve: {
      userDetails: UserDetailsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/user-details-update.component').then(m => m.UserDetailsUpdateComponent),
    resolve: {
      userDetails: UserDetailsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/user-details-update.component').then(m => m.UserDetailsUpdateComponent),
    resolve: {
      userDetails: UserDetailsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default userDetailsRoute;
