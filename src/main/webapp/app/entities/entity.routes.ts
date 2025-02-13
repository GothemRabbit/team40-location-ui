import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'Authorities' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'user-details',
    data: { pageTitle: 'UserDetails' },
    loadChildren: () => import('./user-details/user-details.routes'),
  },
  {
    path: 'authentication',
    data: { pageTitle: 'Authentications' },
    loadChildren: () => import('./authentication/authentication.routes'),
  },
  {
    path: 'item',
    data: { pageTitle: 'Items' },
    loadChildren: () => import('./item/item.routes'),
  },
  {
    path: 'message',
    data: { pageTitle: 'Messages' },
    loadChildren: () => import('./message/message.routes'),
  },
  {
    path: 'conversation',
    data: { pageTitle: 'Conversations' },
    loadChildren: () => import('./conversation/conversation.routes'),
  },
  {
    path: 'product-status',
    data: { pageTitle: 'ProductStatuses' },
    loadChildren: () => import('./product-status/product-status.routes'),
  },
  {
    path: 'user-interaction',
    data: { pageTitle: 'UserInteractions' },
    loadChildren: () => import('./user-interaction/user-interaction.routes'),
  },
  {
    path: 'reservation',
    data: { pageTitle: 'Reservations' },
    loadChildren: () => import('./reservation/reservation.routes'),
  },
  {
    path: 'review',
    data: { pageTitle: 'Reviews' },
    loadChildren: () => import('./review/review.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
