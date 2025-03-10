import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IImages } from '../images.model';
import { ImagesService } from '../service/images.service';

const imagesResolve = (route: ActivatedRouteSnapshot): Observable<null | IImages> => {
  const id = route.params.id;
  if (id) {
    return inject(ImagesService)
      .find(id)
      .pipe(
        mergeMap((images: HttpResponse<IImages>) => {
          if (images.body) {
            return of(images.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default imagesResolve;
