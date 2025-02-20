import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUserInteraction } from '../user-interaction.model';
import { UserInteractionService } from '../service/user-interaction.service';

const userInteractionResolve = (route: ActivatedRouteSnapshot): Observable<null | IUserInteraction> => {
  const id = route.params.id;
  if (id) {
    return inject(UserInteractionService)
      .find(id)
      .pipe(
        mergeMap((userInteraction: HttpResponse<IUserInteraction>) => {
          if (userInteraction.body) {
            return of(userInteraction.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default userInteractionResolve;
