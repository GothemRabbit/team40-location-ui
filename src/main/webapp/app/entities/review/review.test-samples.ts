import dayjs from 'dayjs/esm';

import { IReview, NewReview } from './review.model';

export const sampleWithRequiredData: IReview = {
  id: 10639,
  rating: 0,
  comments: '../fake-data/blob/hipster.txt',
};

export const sampleWithPartialData: IReview = {
  id: 9886,
  rating: 2,
  comments: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: IReview = {
  id: 11617,
  rating: 0,
  comments: '../fake-data/blob/hipster.txt',
  date: dayjs('2025-02-12'),
};

export const sampleWithNewData: NewReview = {
  rating: 4,
  comments: '../fake-data/blob/hipster.txt',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
