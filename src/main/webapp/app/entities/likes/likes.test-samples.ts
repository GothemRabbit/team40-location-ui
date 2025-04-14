import { ILikes, NewLikes } from './likes.model';

export const sampleWithRequiredData: ILikes = {
  id: 12592,
};

export const sampleWithPartialData: ILikes = {
  id: 16706,
};

export const sampleWithFullData: ILikes = {
  id: 29247,
};

export const sampleWithNewData: NewLikes = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
