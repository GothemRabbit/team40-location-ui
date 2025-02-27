import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import UserRecommendationResolve from './route/user-recommendation-routing-resolve.service';

const userRecommendationRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/user-recommendation.component').then(m => m.UserRecommendationComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/user-recommendation-detail.component').then(m => m.UserRecommendationDetailComponent),
    resolve: {
      userRecommendation: UserRecommendationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/user-recommendation-update.component').then(m => m.UserRecommendationUpdateComponent),
    resolve: {
      userRecommendation: UserRecommendationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/user-recommendation-update.component').then(m => m.UserRecommendationUpdateComponent),
    resolve: {
      userRecommendation: UserRecommendationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default userRecommendationRoute;
