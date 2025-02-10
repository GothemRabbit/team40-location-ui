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
    path: 'conversation',
    data: { pageTitle: 'Conversations' },
    loadChildren: () => import('./conversation/conversation.routes'),
  },
  {
    path: 'message',
    data: { pageTitle: 'Messages' },
    loadChildren: () => import('./message/message.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
