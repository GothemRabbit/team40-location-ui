import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ProfileDetailsResolve from './route/profile-details-routing-resolve.service';

const profileDetailsRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/profile-details.component').then(m => m.ProfileDetailsComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/profile-details-detail.component').then(m => m.ProfileDetailsDetailComponent),
    resolve: {
      profileDetails: ProfileDetailsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/profile-details-update.component').then(m => m.ProfileDetailsUpdateComponent),
    resolve: {
      profileDetails: ProfileDetailsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/profile-details-update.component').then(m => m.ProfileDetailsUpdateComponent),
    resolve: {
      profileDetails: ProfileDetailsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default profileDetailsRoute;
