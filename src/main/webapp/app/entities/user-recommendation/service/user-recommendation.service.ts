import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUserRecommendation, NewUserRecommendation } from '../user-recommendation.model';

export type PartialUpdateUserRecommendation = Partial<IUserRecommendation> & Pick<IUserRecommendation, 'id'>;

export type EntityResponseType = HttpResponse<IUserRecommendation>;
export type EntityArrayResponseType = HttpResponse<IUserRecommendation[]>;

@Injectable({ providedIn: 'root' })
export class UserRecommendationService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/user-recommendations');

  create(userRecommendation: NewUserRecommendation): Observable<EntityResponseType> {
    return this.http.post<IUserRecommendation>(this.resourceUrl, userRecommendation, { observe: 'response' });
  }

  update(userRecommendation: IUserRecommendation): Observable<EntityResponseType> {
    return this.http.put<IUserRecommendation>(
      `${this.resourceUrl}/${this.getUserRecommendationIdentifier(userRecommendation)}`,
      userRecommendation,
      { observe: 'response' },
    );
  }

  partialUpdate(userRecommendation: PartialUpdateUserRecommendation): Observable<EntityResponseType> {
    return this.http.patch<IUserRecommendation>(
      `${this.resourceUrl}/${this.getUserRecommendationIdentifier(userRecommendation)}`,
      userRecommendation,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUserRecommendation>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUserRecommendation[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getUserRecommendationIdentifier(userRecommendation: Pick<IUserRecommendation, 'id'>): number {
    return userRecommendation.id;
  }

  compareUserRecommendation(o1: Pick<IUserRecommendation, 'id'> | null, o2: Pick<IUserRecommendation, 'id'> | null): boolean {
    return o1 && o2 ? this.getUserRecommendationIdentifier(o1) === this.getUserRecommendationIdentifier(o2) : o1 === o2;
  }

  addUserRecommendationToCollectionIfMissing<Type extends Pick<IUserRecommendation, 'id'>>(
    userRecommendationCollection: Type[],
    ...userRecommendationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const userRecommendations: Type[] = userRecommendationsToCheck.filter(isPresent);
    if (userRecommendations.length > 0) {
      const userRecommendationCollectionIdentifiers = userRecommendationCollection.map(userRecommendationItem =>
        this.getUserRecommendationIdentifier(userRecommendationItem),
      );
      const userRecommendationsToAdd = userRecommendations.filter(userRecommendationItem => {
        const userRecommendationIdentifier = this.getUserRecommendationIdentifier(userRecommendationItem);
        if (userRecommendationCollectionIdentifiers.includes(userRecommendationIdentifier)) {
          return false;
        }
        userRecommendationCollectionIdentifiers.push(userRecommendationIdentifier);
        return true;
      });
      return [...userRecommendationsToAdd, ...userRecommendationCollection];
    }
    return userRecommendationCollection;
  }
}
