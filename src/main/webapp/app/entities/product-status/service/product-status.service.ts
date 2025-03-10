import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProductStatus, NewProductStatus } from '../product-status.model';

export type PartialUpdateProductStatus = Partial<IProductStatus> & Pick<IProductStatus, 'id'>;

type RestOf<T extends IProductStatus | NewProductStatus> = Omit<T, 'meetingTime' | 'updatedAt' | 'createdAt'> & {
  meetingTime?: string | null;
  updatedAt?: string | null;
  createdAt?: string | null;
};

export type RestProductStatus = RestOf<IProductStatus>;

export type NewRestProductStatus = RestOf<NewProductStatus>;

export type PartialUpdateRestProductStatus = RestOf<PartialUpdateProductStatus>;

export type EntityResponseType = HttpResponse<IProductStatus>;
export type EntityArrayResponseType = HttpResponse<IProductStatus[]>;

@Injectable({ providedIn: 'root' })
export class ProductStatusService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/product-statuses');

  create(productStatus: NewProductStatus): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(productStatus);
    return this.http
      .post<RestProductStatus>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(productStatus: IProductStatus): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(productStatus);
    return this.http
      .put<RestProductStatus>(`${this.resourceUrl}/${this.getProductStatusIdentifier(productStatus)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(productStatus: PartialUpdateProductStatus): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(productStatus);
    return this.http
      .patch<RestProductStatus>(`${this.resourceUrl}/${this.getProductStatusIdentifier(productStatus)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestProductStatus>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestProductStatus[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getProductStatusIdentifier(productStatus: Pick<IProductStatus, 'id'>): number {
    return productStatus.id;
  }

  compareProductStatus(o1: Pick<IProductStatus, 'id'> | null, o2: Pick<IProductStatus, 'id'> | null): boolean {
    return o1 && o2 ? this.getProductStatusIdentifier(o1) === this.getProductStatusIdentifier(o2) : o1 === o2;
  }

  addProductStatusToCollectionIfMissing<Type extends Pick<IProductStatus, 'id'>>(
    productStatusCollection: Type[],
    ...productStatusesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const productStatuses: Type[] = productStatusesToCheck.filter(isPresent);
    if (productStatuses.length > 0) {
      const productStatusCollectionIdentifiers = productStatusCollection.map(productStatusItem =>
        this.getProductStatusIdentifier(productStatusItem),
      );
      const productStatusesToAdd = productStatuses.filter(productStatusItem => {
        const productStatusIdentifier = this.getProductStatusIdentifier(productStatusItem);
        if (productStatusCollectionIdentifiers.includes(productStatusIdentifier)) {
          return false;
        }
        productStatusCollectionIdentifiers.push(productStatusIdentifier);
        return true;
      });
      return [...productStatusesToAdd, ...productStatusCollection];
    }
    return productStatusCollection;
  }

  protected convertDateFromClient<T extends IProductStatus | NewProductStatus | PartialUpdateProductStatus>(productStatus: T): RestOf<T> {
    return {
      ...productStatus,
      meetingTime: productStatus.meetingTime?.toJSON() ?? null,
      updatedAt: productStatus.updatedAt?.toJSON() ?? null,
      createdAt: productStatus.createdAt?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restProductStatus: RestProductStatus): IProductStatus {
    return {
      ...restProductStatus,
      meetingTime: restProductStatus.meetingTime ? dayjs(restProductStatus.meetingTime) : undefined,
      updatedAt: restProductStatus.updatedAt ? dayjs(restProductStatus.updatedAt) : undefined,
      createdAt: restProductStatus.createdAt ? dayjs(restProductStatus.createdAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestProductStatus>): HttpResponse<IProductStatus> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestProductStatus[]>): HttpResponse<IProductStatus[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
