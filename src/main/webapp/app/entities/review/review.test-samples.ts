import dayjs from 'dayjs/esm';

import { IReview, NewReview } from './review.model';

export const sampleWithRequiredData: IReview = {
  id: 5060,
  rating: 2,
  comments: '../fake-data/blob/hipster.txt',
  date: dayjs('2025-02-12'),
};

export const sampleWithPartialData: IReview = {
  id: 31877,
  rating: 0,
  comments: '../fake-data/blob/hipster.txt',
  date: dayjs('2025-02-12'),
};

export const sampleWithFullData: IReview = {
  id: 30614,
  rating: 2,
  comments: '../fake-data/blob/hipster.txt',
  date: dayjs('2025-02-12'),
};

export const sampleWithNewData: NewReview = {
  rating: 0,
  comments: '../fake-data/blob/hipster.txt',
  date: dayjs('2025-02-13'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
