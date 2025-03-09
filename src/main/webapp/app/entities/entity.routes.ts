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
  {
    path: 'notification',
    data: { pageTitle: 'Notifications' },
    loadChildren: () => import('./notification/notification.routes'),
  },
  {
    path: 'user-search-history',
    data: { pageTitle: 'UserSearchHistories' },
    loadChildren: () => import('./user-search-history/user-search-history.routes'),
  },
  {
    path: 'user-recommendation',
    data: { pageTitle: 'UserRecommendations' },
    loadChildren: () => import('./user-recommendation/user-recommendation.routes'),
  },
  {
    path: 'location',
    data: { pageTitle: 'Locations' },
    loadChildren: () => import('./location/location.routes'),
  },
  {
    path: 'likes',
    data: { pageTitle: 'Likes' },
    loadChildren: () => import('./likes/likes.routes'),
  },
  {
    path: 'images',
    data: { pageTitle: 'Images' },
    loadChildren: () => import('./images/images.routes'),
  },
  {
    path: 'wishlist',
    data: { pageTitle: 'Wishlists' },
    loadChildren: () => import('./wishlist/wishlist.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
