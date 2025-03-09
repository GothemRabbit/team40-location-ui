import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProfileDetails, NewProfileDetails } from '../profile-details.model';

export type PartialUpdateProfileDetails = Partial<IProfileDetails> & Pick<IProfileDetails, 'id'>;

type RestOf<T extends IProfileDetails | NewProfileDetails> = Omit<T, 'birthDate'> & {
  birthDate?: string | null;
};

export type RestProfileDetails = RestOf<IProfileDetails>;

export type NewRestProfileDetails = RestOf<NewProfileDetails>;

export type PartialUpdateRestProfileDetails = RestOf<PartialUpdateProfileDetails>;

export type EntityResponseType = HttpResponse<IProfileDetails>;
export type EntityArrayResponseType = HttpResponse<IProfileDetails[]>;

@Injectable({ providedIn: 'root' })
export class ProfileDetailsService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/profile-details');

  create(profileDetails: NewProfileDetails): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(profileDetails);
    return this.http
      .post<RestProfileDetails>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(profileDetails: IProfileDetails): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(profileDetails);
    return this.http
      .put<RestProfileDetails>(`${this.resourceUrl}/${this.getProfileDetailsIdentifier(profileDetails)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(profileDetails: PartialUpdateProfileDetails): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(profileDetails);
    return this.http
      .patch<RestProfileDetails>(`${this.resourceUrl}/${this.getProfileDetailsIdentifier(profileDetails)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestProfileDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestProfileDetails[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getProfileDetailsIdentifier(profileDetails: Pick<IProfileDetails, 'id'>): number {
    return profileDetails.id;
  }

  compareProfileDetails(o1: Pick<IProfileDetails, 'id'> | null, o2: Pick<IProfileDetails, 'id'> | null): boolean {
    return o1 && o2 ? this.getProfileDetailsIdentifier(o1) === this.getProfileDetailsIdentifier(o2) : o1 === o2;
  }

  addProfileDetailsToCollectionIfMissing<Type extends Pick<IProfileDetails, 'id'>>(
    profileDetailsCollection: Type[],
    ...profileDetailsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const profileDetails: Type[] = profileDetailsToCheck.filter(isPresent);
    if (profileDetails.length > 0) {
      const profileDetailsCollectionIdentifiers = profileDetailsCollection.map(profileDetailsItem =>
        this.getProfileDetailsIdentifier(profileDetailsItem),
      );
      const profileDetailsToAdd = profileDetails.filter(profileDetailsItem => {
        const profileDetailsIdentifier = this.getProfileDetailsIdentifier(profileDetailsItem);
        if (profileDetailsCollectionIdentifiers.includes(profileDetailsIdentifier)) {
          return false;
        }
        profileDetailsCollectionIdentifiers.push(profileDetailsIdentifier);
        return true;
      });
      return [...profileDetailsToAdd, ...profileDetailsCollection];
    }
    return profileDetailsCollection;
  }

  protected convertDateFromClient<T extends IProfileDetails | NewProfileDetails | PartialUpdateProfileDetails>(
    profileDetails: T,
  ): RestOf<T> {
    return {
      ...profileDetails,
      birthDate: profileDetails.birthDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restProfileDetails: RestProfileDetails): IProfileDetails {
    return {
      ...restProfileDetails,
      birthDate: restProfileDetails.birthDate ? dayjs(restProfileDetails.birthDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestProfileDetails>): HttpResponse<IProfileDetails> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestProfileDetails[]>): HttpResponse<IProfileDetails[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
