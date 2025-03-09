import { ILikes, NewLikes } from './likes.model';

export const sampleWithRequiredData: ILikes = {
  id: 25096,
};

export const sampleWithPartialData: ILikes = {
  id: 29574,
};

export const sampleWithFullData: ILikes = {
  id: 25640,
  liked: false,
};

export const sampleWithNewData: NewLikes = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
