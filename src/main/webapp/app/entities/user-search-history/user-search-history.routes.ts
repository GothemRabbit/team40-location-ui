import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import UserSearchHistoryResolve from './route/user-search-history-routing-resolve.service';

const userSearchHistoryRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/user-search-history.component').then(m => m.UserSearchHistoryComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/user-search-history-detail.component').then(m => m.UserSearchHistoryDetailComponent),
    resolve: {
      userSearchHistory: UserSearchHistoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/user-search-history-update.component').then(m => m.UserSearchHistoryUpdateComponent),
    resolve: {
      userSearchHistory: UserSearchHistoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/user-search-history-update.component').then(m => m.UserSearchHistoryUpdateComponent),
    resolve: {
      userSearchHistory: UserSearchHistoryResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default userSearchHistoryRoute;
