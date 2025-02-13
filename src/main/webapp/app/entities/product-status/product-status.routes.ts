import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ProductStatusResolve from './route/product-status-routing-resolve.service';

const productStatusRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/product-status.component').then(m => m.ProductStatusComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/product-status-detail.component').then(m => m.ProductStatusDetailComponent),
    resolve: {
      productStatus: ProductStatusResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/product-status-update.component').then(m => m.ProductStatusUpdateComponent),
    resolve: {
      productStatus: ProductStatusResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/product-status-update.component').then(m => m.ProductStatusUpdateComponent),
    resolve: {
      productStatus: ProductStatusResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default productStatusRoute;
