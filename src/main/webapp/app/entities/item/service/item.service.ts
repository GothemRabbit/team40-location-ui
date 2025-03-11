import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IItem, NewItem } from '../item.model';
import { IImages } from '../../images/images.model';

export type PartialUpdateItem = Partial<IItem> & Pick<IItem, 'id'>;

type RestOf<T extends IItem | NewItem> = Omit<T, 'timeListed'> & {
  timeListed?: string | null;
};

export type RestItem = RestOf<IItem>;

export type NewRestItem = RestOf<NewItem>;

export type PartialUpdateRestItem = RestOf<PartialUpdateItem>;

export type EntityResponseType = HttpResponse<IItem>;
export type EntityArrayResponseType = HttpResponse<IItem[]>;

@Injectable({ providedIn: 'root' })
export class ItemService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/items');

  create(item: NewItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(item);
    return this.http.post<RestItem>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(item: IItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(item);
    return this.http
      .put<RestItem>(`${this.resourceUrl}/${this.getItemIdentifier(item)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(item: PartialUpdateItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(item);
    return this.http
      .patch<RestItem>(`${this.resourceUrl}/${this.getItemIdentifier(item)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestItem>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  // find(id: number): Observable<IItem> {
  //   return this.http
  //     .get<IItem>(`${this.resourceUrl}/${id}`)
  //     .pipe(map(item => ({
  //       ...item,
  //       images: item.images || [] // Ensure images are initialized
  //     })));
  // }

  getImagesForItem(itemId: number): Observable<IImages[]> {
    return this.http.get<IImages[]>(`http://localhost:8080/api/items/${itemId}/images`);
  }
  // find(id: number): Observable<IItem> {
  //   return this.http.get<IItem>(`${this.resourceUrl}/${id}?eagerload=true`); // Ensures images are loaded
  // }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestItem[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  // //for like
  // query(req?: any): Observable<EntityArrayResponseType> {
  //   const options = createRequestOption(req);
  //   return this.http.get<IItem[]>(this.resourceUrl, { params: options, observe: 'response' });
  // }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getItemIdentifier(item: Pick<IItem, 'id'>): number {
    return item.id;
  }

  compareItem(o1: Pick<IItem, 'id'> | null, o2: Pick<IItem, 'id'> | null): boolean {
    return o1 && o2 ? this.getItemIdentifier(o1) === this.getItemIdentifier(o2) : o1 === o2;
  }

  addItemToCollectionIfMissing<Type extends Pick<IItem, 'id'>>(
    itemCollection: Type[],
    ...itemsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const items: Type[] = itemsToCheck.filter(isPresent);
    if (items.length > 0) {
      const itemCollectionIdentifiers = itemCollection.map(itemItem => this.getItemIdentifier(itemItem));
      const itemsToAdd = items.filter(itemItem => {
        const itemIdentifier = this.getItemIdentifier(itemItem);
        if (itemCollectionIdentifiers.includes(itemIdentifier)) {
          return false;
        }
        itemCollectionIdentifiers.push(itemIdentifier);
        return true;
      });
      return [...itemsToAdd, ...itemCollection];
    }
    return itemCollection;
  }

  protected convertDateFromClient<T extends IItem | NewItem | PartialUpdateItem>(item: T): RestOf<T> {
    return {
      ...item,
      timeListed: item.timeListed?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restItem: RestItem): IItem {
    return {
      ...restItem,
      timeListed: restItem.timeListed ? dayjs(restItem.timeListed) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestItem>): HttpResponse<IItem> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestItem[]>): HttpResponse<IItem[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
