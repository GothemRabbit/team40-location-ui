import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import ConversationResolve from './route/conversation-routing-resolve.service';
import { ConversationMessagesComponent } from './conversation-messages.component';

const conversationRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/conversation.component').then(m => m.ConversationComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/conversation-detail.component').then(m => m.ConversationDetailComponent),
    resolve: {
      conversation: ConversationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/conversation-update.component').then(m => m.ConversationUpdateComponent),
    resolve: {
      conversation: ConversationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/conversation-update.component').then(m => m.ConversationUpdateComponent),
    resolve: {
      conversation: ConversationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/messages',
    component: ConversationMessagesComponent,
    canActivate: [UserRouteAccessService],
    title: 'Conversation Messages',
  },
];

export default conversationRoute;
