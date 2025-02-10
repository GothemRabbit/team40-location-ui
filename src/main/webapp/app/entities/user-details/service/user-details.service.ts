import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUserDetails, NewUserDetails } from '../user-details.model';

export type PartialUpdateUserDetails = Partial<IUserDetails> & Pick<IUserDetails, 'id'>;

type RestOf<T extends IUserDetails | NewUserDetails> = Omit<T, 'lastActive'> & {
  lastActive?: string | null;
};

export type RestUserDetails = RestOf<IUserDetails>;

export type NewRestUserDetails = RestOf<NewUserDetails>;

export type PartialUpdateRestUserDetails = RestOf<PartialUpdateUserDetails>;

export type EntityResponseType = HttpResponse<IUserDetails>;
export type EntityArrayResponseType = HttpResponse<IUserDetails[]>;

@Injectable({ providedIn: 'root' })
export class UserDetailsService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/user-details');

  create(userDetails: NewUserDetails): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(userDetails);
    return this.http
      .post<RestUserDetails>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(userDetails: IUserDetails): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(userDetails);
    return this.http
      .put<RestUserDetails>(`${this.resourceUrl}/${this.getUserDetailsIdentifier(userDetails)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(userDetails: PartialUpdateUserDetails): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(userDetails);
    return this.http
      .patch<RestUserDetails>(`${this.resourceUrl}/${this.getUserDetailsIdentifier(userDetails)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestUserDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestUserDetails[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getUserDetailsIdentifier(userDetails: Pick<IUserDetails, 'id'>): number {
    return userDetails.id;
  }

  compareUserDetails(o1: Pick<IUserDetails, 'id'> | null, o2: Pick<IUserDetails, 'id'> | null): boolean {
    return o1 && o2 ? this.getUserDetailsIdentifier(o1) === this.getUserDetailsIdentifier(o2) : o1 === o2;
  }

  addUserDetailsToCollectionIfMissing<Type extends Pick<IUserDetails, 'id'>>(
    userDetailsCollection: Type[],
    ...userDetailsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const userDetails: Type[] = userDetailsToCheck.filter(isPresent);
    if (userDetails.length > 0) {
      const userDetailsCollectionIdentifiers = userDetailsCollection.map(userDetailsItem => this.getUserDetailsIdentifier(userDetailsItem));
      const userDetailsToAdd = userDetails.filter(userDetailsItem => {
        const userDetailsIdentifier = this.getUserDetailsIdentifier(userDetailsItem);
        if (userDetailsCollectionIdentifiers.includes(userDetailsIdentifier)) {
          return false;
        }
        userDetailsCollectionIdentifiers.push(userDetailsIdentifier);
        return true;
      });
      return [...userDetailsToAdd, ...userDetailsCollection];
    }
    return userDetailsCollection;
  }

  protected convertDateFromClient<T extends IUserDetails | NewUserDetails | PartialUpdateUserDetails>(userDetails: T): RestOf<T> {
    return {
      ...userDetails,
      lastActive: userDetails.lastActive?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restUserDetails: RestUserDetails): IUserDetails {
    return {
      ...restUserDetails,
      lastActive: restUserDetails.lastActive ? dayjs(restUserDetails.lastActive) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestUserDetails>): HttpResponse<IUserDetails> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestUserDetails[]>): HttpResponse<IUserDetails[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
