import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IWishlist, NewWishlist } from '../wishlist.model';

export type PartialUpdateWishlist = Partial<IWishlist> & Pick<IWishlist, 'id'>;

export type EntityResponseType = HttpResponse<IWishlist>;
export type EntityArrayResponseType = HttpResponse<IWishlist[]>;

@Injectable({ providedIn: 'root' })
export class WishlistService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/wishlists');

  create(wishlist: NewWishlist): Observable<EntityResponseType> {
    return this.http.post<IWishlist>(this.resourceUrl, wishlist, { observe: 'response' });
  }

  update(wishlist: IWishlist): Observable<EntityResponseType> {
    return this.http.put<IWishlist>(`${this.resourceUrl}/${this.getWishlistIdentifier(wishlist)}`, wishlist, { observe: 'response' });
  }

  partialUpdate(wishlist: PartialUpdateWishlist): Observable<EntityResponseType> {
    return this.http.patch<IWishlist>(`${this.resourceUrl}/${this.getWishlistIdentifier(wishlist)}`, wishlist, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IWishlist>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IWishlist[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getWishlistIdentifier(wishlist: Pick<IWishlist, 'id'>): number {
    return wishlist.id;
  }

  compareWishlist(o1: Pick<IWishlist, 'id'> | null, o2: Pick<IWishlist, 'id'> | null): boolean {
    return o1 && o2 ? this.getWishlistIdentifier(o1) === this.getWishlistIdentifier(o2) : o1 === o2;
  }

  addWishlistToCollectionIfMissing<Type extends Pick<IWishlist, 'id'>>(
    wishlistCollection: Type[],
    ...wishlistsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const wishlists: Type[] = wishlistsToCheck.filter(isPresent);
    if (wishlists.length > 0) {
      const wishlistCollectionIdentifiers = wishlistCollection.map(wishlistItem => this.getWishlistIdentifier(wishlistItem));
      const wishlistsToAdd = wishlists.filter(wishlistItem => {
        const wishlistIdentifier = this.getWishlistIdentifier(wishlistItem);
        if (wishlistCollectionIdentifiers.includes(wishlistIdentifier)) {
          return false;
        }
        wishlistCollectionIdentifiers.push(wishlistIdentifier);
        return true;
      });
      return [...wishlistsToAdd, ...wishlistCollection];
    }
    return wishlistCollection;
  }
}
