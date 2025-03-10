import { ILikes, NewLikes } from './likes.model';

export const sampleWithRequiredData: ILikes = {
  id: 16706,
};

export const sampleWithPartialData: ILikes = {
  id: 25908,
};

export const sampleWithFullData: ILikes = {
  id: 20840,
  liked: true,
};

export const sampleWithNewData: NewLikes = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
