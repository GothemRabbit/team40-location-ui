import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUserSearchHistory } from '../user-search-history.model';
import { UserSearchHistoryService } from '../service/user-search-history.service';

const userSearchHistoryResolve = (route: ActivatedRouteSnapshot): Observable<null | IUserSearchHistory> => {
  const id = route.params.id;
  if (id) {
    return inject(UserSearchHistoryService)
      .find(id)
      .pipe(
        mergeMap((userSearchHistory: HttpResponse<IUserSearchHistory>) => {
          if (userSearchHistory.body) {
            return of(userSearchHistory.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default userSearchHistoryResolve;
