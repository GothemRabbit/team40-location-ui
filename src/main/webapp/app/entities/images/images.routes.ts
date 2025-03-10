import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ImagesResolve from './route/images-routing-resolve.service';

const imagesRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/images.component').then(m => m.ImagesComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/images-detail.component').then(m => m.ImagesDetailComponent),
    resolve: {
      images: ImagesResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/images-update.component').then(m => m.ImagesUpdateComponent),
    resolve: {
      images: ImagesResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/images-update.component').then(m => m.ImagesUpdateComponent),
    resolve: {
      images: ImagesResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default imagesRoute;
