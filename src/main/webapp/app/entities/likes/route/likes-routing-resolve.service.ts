import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILikes } from '../likes.model';
import { LikesService } from '../service/likes.service';

const likesResolve = (route: ActivatedRouteSnapshot): Observable<null | ILikes> => {
  const id = route.params.id;
  if (id) {
    return inject(LikesService)
      .find(id)
      .pipe(
        mergeMap((likes: HttpResponse<ILikes>) => {
          if (likes.body) {
            return of(likes.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default likesResolve;
