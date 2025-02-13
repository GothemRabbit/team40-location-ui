import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAuthentication, NewAuthentication } from '../authentication.model';

export type PartialUpdateAuthentication = Partial<IAuthentication> & Pick<IAuthentication, 'id'>;

export type EntityResponseType = HttpResponse<IAuthentication>;
export type EntityArrayResponseType = HttpResponse<IAuthentication[]>;

@Injectable({ providedIn: 'root' })
export class AuthenticationService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/authentications');

  create(authentication: NewAuthentication): Observable<EntityResponseType> {
    return this.http.post<IAuthentication>(this.resourceUrl, authentication, { observe: 'response' });
  }

  update(authentication: IAuthentication): Observable<EntityResponseType> {
    return this.http.put<IAuthentication>(`${this.resourceUrl}/${this.getAuthenticationIdentifier(authentication)}`, authentication, {
      observe: 'response',
    });
  }

  partialUpdate(authentication: PartialUpdateAuthentication): Observable<EntityResponseType> {
    return this.http.patch<IAuthentication>(`${this.resourceUrl}/${this.getAuthenticationIdentifier(authentication)}`, authentication, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAuthentication>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAuthentication[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAuthenticationIdentifier(authentication: Pick<IAuthentication, 'id'>): number {
    return authentication.id;
  }

  compareAuthentication(o1: Pick<IAuthentication, 'id'> | null, o2: Pick<IAuthentication, 'id'> | null): boolean {
    return o1 && o2 ? this.getAuthenticationIdentifier(o1) === this.getAuthenticationIdentifier(o2) : o1 === o2;
  }

  addAuthenticationToCollectionIfMissing<Type extends Pick<IAuthentication, 'id'>>(
    authenticationCollection: Type[],
    ...authenticationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const authentications: Type[] = authenticationsToCheck.filter(isPresent);
    if (authentications.length > 0) {
      const authenticationCollectionIdentifiers = authenticationCollection.map(authenticationItem =>
        this.getAuthenticationIdentifier(authenticationItem),
      );
      const authenticationsToAdd = authentications.filter(authenticationItem => {
        const authenticationIdentifier = this.getAuthenticationIdentifier(authenticationItem);
        if (authenticationCollectionIdentifiers.includes(authenticationIdentifier)) {
          return false;
        }
        authenticationCollectionIdentifiers.push(authenticationIdentifier);
        return true;
      });
      return [...authenticationsToAdd, ...authenticationCollection];
    }
    return authenticationCollection;
  }
}
