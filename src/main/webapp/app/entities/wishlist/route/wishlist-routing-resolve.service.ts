import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IWishlist } from '../wishlist.model';
import { WishlistService } from '../service/wishlist.service';

const wishlistResolve = (route: ActivatedRouteSnapshot): Observable<null | IWishlist> => {
  const id = route.params.id;
  if (id) {
    return inject(WishlistService)
      .find(id)
      .pipe(
        mergeMap((wishlist: HttpResponse<IWishlist>) => {
          if (wishlist.body) {
            return of(wishlist.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default wishlistResolve;
