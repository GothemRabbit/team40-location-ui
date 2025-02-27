import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ItemResolve from './route/item-routing-resolve.service';

const itemRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/item.component').then(m => m.ItemComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/item-detail.component').then(m => m.ItemDetailComponent),
    resolve: {
      item: ItemResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/item-update.component').then(m => m.ItemUpdateComponent),
    resolve: {
      item: ItemResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/item-update.component').then(m => m.ItemUpdateComponent),
    resolve: {
      item: ItemResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default itemRoute;
