import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IImages, NewImages } from '../images.model';

export type PartialUpdateImages = Partial<IImages> & Pick<IImages, 'id'>;

export type EntityResponseType = HttpResponse<IImages>;
export type EntityArrayResponseType = HttpResponse<IImages[]>;

@Injectable({ providedIn: 'root' })
export class ImagesService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/images');

  create(images: NewImages): Observable<EntityResponseType> {
    return this.http.post<IImages>(this.resourceUrl, images, { observe: 'response' });
  }

  update(images: IImages): Observable<EntityResponseType> {
    return this.http.put<IImages>(`${this.resourceUrl}/${this.getImagesIdentifier(images)}`, images, { observe: 'response' });
  }

  partialUpdate(images: PartialUpdateImages): Observable<EntityResponseType> {
    return this.http.patch<IImages>(`${this.resourceUrl}/${this.getImagesIdentifier(images)}`, images, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IImages>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IImages[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getImagesIdentifier(images: Pick<IImages, 'id'>): number {
    return images.id;
  }

  compareImages(o1: Pick<IImages, 'id'> | null, o2: Pick<IImages, 'id'> | null): boolean {
    return o1 && o2 ? this.getImagesIdentifier(o1) === this.getImagesIdentifier(o2) : o1 === o2;
  }

  addImagesToCollectionIfMissing<Type extends Pick<IImages, 'id'>>(
    imagesCollection: Type[],
    ...imagesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const images: Type[] = imagesToCheck.filter(isPresent);
    if (images.length > 0) {
      const imagesCollectionIdentifiers = imagesCollection.map(imagesItem => this.getImagesIdentifier(imagesItem));
      const imagesToAdd = images.filter(imagesItem => {
        const imagesIdentifier = this.getImagesIdentifier(imagesItem);
        if (imagesCollectionIdentifiers.includes(imagesIdentifier)) {
          return false;
        }
        imagesCollectionIdentifiers.push(imagesIdentifier);
        return true;
      });
      return [...imagesToAdd, ...imagesCollection];
    }
    return imagesCollection;
  }
}
