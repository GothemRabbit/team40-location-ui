import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUserSearchHistory, NewUserSearchHistory } from '../user-search-history.model';

export type PartialUpdateUserSearchHistory = Partial<IUserSearchHistory> & Pick<IUserSearchHistory, 'id'>;

type RestOf<T extends IUserSearchHistory | NewUserSearchHistory> = Omit<T, 'searchDate'> & {
  searchDate?: string | null;
};

export type RestUserSearchHistory = RestOf<IUserSearchHistory>;

export type NewRestUserSearchHistory = RestOf<NewUserSearchHistory>;

export type PartialUpdateRestUserSearchHistory = RestOf<PartialUpdateUserSearchHistory>;

export type EntityResponseType = HttpResponse<IUserSearchHistory>;
export type EntityArrayResponseType = HttpResponse<IUserSearchHistory[]>;

@Injectable({ providedIn: 'root' })
export class UserSearchHistoryService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/user-search-histories');

  create(userSearchHistory: NewUserSearchHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(userSearchHistory);
    return this.http
      .post<RestUserSearchHistory>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(userSearchHistory: IUserSearchHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(userSearchHistory);
    return this.http
      .put<RestUserSearchHistory>(`${this.resourceUrl}/${this.getUserSearchHistoryIdentifier(userSearchHistory)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(userSearchHistory: PartialUpdateUserSearchHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(userSearchHistory);
    return this.http
      .patch<RestUserSearchHistory>(`${this.resourceUrl}/${this.getUserSearchHistoryIdentifier(userSearchHistory)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestUserSearchHistory>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestUserSearchHistory[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getUserSearchHistoryIdentifier(userSearchHistory: Pick<IUserSearchHistory, 'id'>): number {
    return userSearchHistory.id;
  }

  compareUserSearchHistory(o1: Pick<IUserSearchHistory, 'id'> | null, o2: Pick<IUserSearchHistory, 'id'> | null): boolean {
    return o1 && o2 ? this.getUserSearchHistoryIdentifier(o1) === this.getUserSearchHistoryIdentifier(o2) : o1 === o2;
  }

  addUserSearchHistoryToCollectionIfMissing<Type extends Pick<IUserSearchHistory, 'id'>>(
    userSearchHistoryCollection: Type[],
    ...userSearchHistoriesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const userSearchHistories: Type[] = userSearchHistoriesToCheck.filter(isPresent);
    if (userSearchHistories.length > 0) {
      const userSearchHistoryCollectionIdentifiers = userSearchHistoryCollection.map(userSearchHistoryItem =>
        this.getUserSearchHistoryIdentifier(userSearchHistoryItem),
      );
      const userSearchHistoriesToAdd = userSearchHistories.filter(userSearchHistoryItem => {
        const userSearchHistoryIdentifier = this.getUserSearchHistoryIdentifier(userSearchHistoryItem);
        if (userSearchHistoryCollectionIdentifiers.includes(userSearchHistoryIdentifier)) {
          return false;
        }
        userSearchHistoryCollectionIdentifiers.push(userSearchHistoryIdentifier);
        return true;
      });
      return [...userSearchHistoriesToAdd, ...userSearchHistoryCollection];
    }
    return userSearchHistoryCollection;
  }

  protected convertDateFromClient<T extends IUserSearchHistory | NewUserSearchHistory | PartialUpdateUserSearchHistory>(
    userSearchHistory: T,
  ): RestOf<T> {
    return {
      ...userSearchHistory,
      searchDate: userSearchHistory.searchDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restUserSearchHistory: RestUserSearchHistory): IUserSearchHistory {
    return {
      ...restUserSearchHistory,
      searchDate: restUserSearchHistory.searchDate ? dayjs(restUserSearchHistory.searchDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestUserSearchHistory>): HttpResponse<IUserSearchHistory> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestUserSearchHistory[]>): HttpResponse<IUserSearchHistory[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
