import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILikes, NewLikes } from '../likes.model';

export type PartialUpdateLikes = Partial<ILikes> & Pick<ILikes, 'id'>;

export type EntityResponseType = HttpResponse<ILikes>;
export type EntityArrayResponseType = HttpResponse<ILikes[]>;

@Injectable({ providedIn: 'root' })
export class LikesService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/likes');

  create(likes: NewLikes): Observable<EntityResponseType> {
    return this.http.post<ILikes>(this.resourceUrl, likes, { observe: 'response' });
  }

  update(likes: ILikes): Observable<EntityResponseType> {
    return this.http.put<ILikes>(`${this.resourceUrl}/${this.getLikesIdentifier(likes)}`, likes, { observe: 'response' });
  }

  partialUpdate(likes: PartialUpdateLikes): Observable<EntityResponseType> {
    return this.http.patch<ILikes>(`${this.resourceUrl}/${this.getLikesIdentifier(likes)}`, likes, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILikes>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILikes[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getLikesIdentifier(likes: Pick<ILikes, 'id'>): number {
    return likes.id;
  }

  compareLikes(o1: Pick<ILikes, 'id'> | null, o2: Pick<ILikes, 'id'> | null): boolean {
    return o1 && o2 ? this.getLikesIdentifier(o1) === this.getLikesIdentifier(o2) : o1 === o2;
  }

  addLikesToCollectionIfMissing<Type extends Pick<ILikes, 'id'>>(
    likesCollection: Type[],
    ...likesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const likes: Type[] = likesToCheck.filter(isPresent);
    if (likes.length > 0) {
      const likesCollectionIdentifiers = likesCollection.map(likesItem => this.getLikesIdentifier(likesItem));
      const likesToAdd = likes.filter(likesItem => {
        const likesIdentifier = this.getLikesIdentifier(likesItem);
        if (likesCollectionIdentifiers.includes(likesIdentifier)) {
          return false;
        }
        likesCollectionIdentifiers.push(likesIdentifier);
        return true;
      });
      return [...likesToAdd, ...likesCollection];
    }
    return likesCollection;
  }
}
