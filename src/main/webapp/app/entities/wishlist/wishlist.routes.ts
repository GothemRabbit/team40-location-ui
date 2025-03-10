import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import WishlistResolve from './route/wishlist-routing-resolve.service';

const wishlistRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/wishlist.component').then(m => m.WishlistComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/wishlist-detail.component').then(m => m.WishlistDetailComponent),
    resolve: {
      wishlist: WishlistResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/wishlist-update.component').then(m => m.WishlistUpdateComponent),
    resolve: {
      wishlist: WishlistResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/wishlist-update.component').then(m => m.WishlistUpdateComponent),
    resolve: {
      wishlist: WishlistResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default wishlistRoute;
