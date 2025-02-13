import dayjs from 'dayjs/esm';

import { IReview, NewReview } from './review.model';

export const sampleWithRequiredData: IReview = {
  id: 27180,
  rating: 3.23,
  reviewDate: dayjs('2025-02-12'),
};

export const sampleWithPartialData: IReview = {
  id: 20874,
  rating: 1.83,
  reviewText: '../fake-data/blob/hipster.txt',
  reviewDate: dayjs('2025-02-13'),
};

export const sampleWithFullData: IReview = {
  id: 19666,
  rating: 0.87,
  reviewText: '../fake-data/blob/hipster.txt',
  reviewDate: dayjs('2025-02-13'),
};

export const sampleWithNewData: NewReview = {
  rating: 1.44,
  reviewDate: dayjs('2025-02-12'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
