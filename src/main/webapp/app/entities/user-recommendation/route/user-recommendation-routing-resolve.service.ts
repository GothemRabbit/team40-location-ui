import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUserRecommendation } from '../user-recommendation.model';
import { UserRecommendationService } from '../service/user-recommendation.service';

const userRecommendationResolve = (route: ActivatedRouteSnapshot): Observable<null | IUserRecommendation> => {
  const id = route.params.id;
  if (id) {
    return inject(UserRecommendationService)
      .find(id)
      .pipe(
        mergeMap((userRecommendation: HttpResponse<IUserRecommendation>) => {
          if (userRecommendation.body) {
            return of(userRecommendation.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default userRecommendationResolve;
