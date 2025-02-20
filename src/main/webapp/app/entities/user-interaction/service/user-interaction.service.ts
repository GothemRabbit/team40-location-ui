import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUserInteraction, NewUserInteraction } from '../user-interaction.model';

export type PartialUpdateUserInteraction = Partial<IUserInteraction> & Pick<IUserInteraction, 'id'>;

type RestOf<T extends IUserInteraction | NewUserInteraction> = Omit<T, 'interactionDate'> & {
  interactionDate?: string | null;
};

export type RestUserInteraction = RestOf<IUserInteraction>;

export type NewRestUserInteraction = RestOf<NewUserInteraction>;

export type PartialUpdateRestUserInteraction = RestOf<PartialUpdateUserInteraction>;

export type EntityResponseType = HttpResponse<IUserInteraction>;
export type EntityArrayResponseType = HttpResponse<IUserInteraction[]>;

@Injectable({ providedIn: 'root' })
export class UserInteractionService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/user-interactions');

  create(userInteraction: NewUserInteraction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(userInteraction);
    return this.http
      .post<RestUserInteraction>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(userInteraction: IUserInteraction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(userInteraction);
    return this.http
      .put<RestUserInteraction>(`${this.resourceUrl}/${this.getUserInteractionIdentifier(userInteraction)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(userInteraction: PartialUpdateUserInteraction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(userInteraction);
    return this.http
      .patch<RestUserInteraction>(`${this.resourceUrl}/${this.getUserInteractionIdentifier(userInteraction)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestUserInteraction>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestUserInteraction[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getUserInteractionIdentifier(userInteraction: Pick<IUserInteraction, 'id'>): number {
    return userInteraction.id;
  }

  compareUserInteraction(o1: Pick<IUserInteraction, 'id'> | null, o2: Pick<IUserInteraction, 'id'> | null): boolean {
    return o1 && o2 ? this.getUserInteractionIdentifier(o1) === this.getUserInteractionIdentifier(o2) : o1 === o2;
  }

  addUserInteractionToCollectionIfMissing<Type extends Pick<IUserInteraction, 'id'>>(
    userInteractionCollection: Type[],
    ...userInteractionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const userInteractions: Type[] = userInteractionsToCheck.filter(isPresent);
    if (userInteractions.length > 0) {
      const userInteractionCollectionIdentifiers = userInteractionCollection.map(userInteractionItem =>
        this.getUserInteractionIdentifier(userInteractionItem),
      );
      const userInteractionsToAdd = userInteractions.filter(userInteractionItem => {
        const userInteractionIdentifier = this.getUserInteractionIdentifier(userInteractionItem);
        if (userInteractionCollectionIdentifiers.includes(userInteractionIdentifier)) {
          return false;
        }
        userInteractionCollectionIdentifiers.push(userInteractionIdentifier);
        return true;
      });
      return [...userInteractionsToAdd, ...userInteractionCollection];
    }
    return userInteractionCollection;
  }

  protected convertDateFromClient<T extends IUserInteraction | NewUserInteraction | PartialUpdateUserInteraction>(
    userInteraction: T,
  ): RestOf<T> {
    return {
      ...userInteraction,
      interactionDate: userInteraction.interactionDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restUserInteraction: RestUserInteraction): IUserInteraction {
    return {
      ...restUserInteraction,
      interactionDate: restUserInteraction.interactionDate ? dayjs(restUserInteraction.interactionDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestUserInteraction>): HttpResponse<IUserInteraction> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestUserInteraction[]>): HttpResponse<IUserInteraction[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
