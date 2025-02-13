import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAuthentication } from '../authentication.model';
import { AuthenticationService } from '../service/authentication.service';

const authenticationResolve = (route: ActivatedRouteSnapshot): Observable<null | IAuthentication> => {
  const id = route.params.id;
  if (id) {
    return inject(AuthenticationService)
      .find(id)
      .pipe(
        mergeMap((authentication: HttpResponse<IAuthentication>) => {
          if (authentication.body) {
            return of(authentication.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default authenticationResolve;
