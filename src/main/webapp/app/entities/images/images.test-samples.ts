import { IImages, NewImages } from './images.model';

export const sampleWithRequiredData: IImages = {
  id: 9658,
  images: '../fake-data/blob/hipster.png',
  imagesContentType: 'unknown',
};

export const sampleWithPartialData: IImages = {
  id: 32396,
  images: '../fake-data/blob/hipster.png',
  imagesContentType: 'unknown',
};

export const sampleWithFullData: IImages = {
  id: 5633,
  images: '../fake-data/blob/hipster.png',
  imagesContentType: 'unknown',
};

export const sampleWithNewData: NewImages = {
  images: '../fake-data/blob/hipster.png',
  imagesContentType: 'unknown',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
