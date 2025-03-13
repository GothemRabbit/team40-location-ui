import { Routes } from '@angular/router';

import activateRoute from './activate/activate.route';
import passwordRoute from './password/password.route';
import passwordResetFinishRoute from './password-reset/finish/password-reset-finish.route';
import passwordResetInitRoute from './password-reset/init/password-reset-init.route';
import registerRoute from './register/register.route';
import settingsRoute from './settings/settings.route';
import { Authority } from '../config/authority.constants';
import { UserRouteAccessService } from '../core/auth/user-route-access.service';

const accountRoutes: Routes = [
  activateRoute,
  passwordRoute,
  passwordResetFinishRoute,
  passwordResetInitRoute,
  registerRoute,
  settingsRoute,

  // {
  //   path: 'profile-details',
  //   component: ProfileDetailComponent,
  //   canActivate: [UserRouteAccessService],
  //   data: { authorities: [Authority.ADMIN] }, // Admin access to any profile
  // },
  // {
  //   path: 'profile-details/:id',
  //   component: ProfileDetailComponent,
  //   canActivate: [UserRouteAccessService],
  //   data: { authorities: [Authority.USER, Authority.ADMIN] }, // Regular users can only access their own profile
  // },
];

export default accountRoutes;
