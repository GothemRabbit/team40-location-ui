import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProfileDetails } from '../profile-details.model';
import { ProfileDetailsService } from '../service/profile-details.service';

const profileDetailsResolve = (route: ActivatedRouteSnapshot): Observable<null | IProfileDetails> => {
  const id = route.params.id;
  if (id) {
    return inject(ProfileDetailsService)
      .find(id)
      .pipe(
        mergeMap((profileDetails: HttpResponse<IProfileDetails>) => {
          if (profileDetails.body) {
            return of(profileDetails.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default profileDetailsResolve;
