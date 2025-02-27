import dayjs from 'dayjs/esm';

import { IReview, NewReview } from './review.model';

export const sampleWithRequiredData: IReview = {
  id: 29632,
  rating: 0,
  reviewDate: dayjs('2025-02-12'),
};

export const sampleWithPartialData: IReview = {
  id: 10870,
  rating: 5,
  reviewText: '../fake-data/blob/hipster.txt',
  reviewDate: dayjs('2025-02-12'),
};

export const sampleWithFullData: IReview = {
  id: 4533,
  rating: 5,
  reviewText: '../fake-data/blob/hipster.txt',
  reviewDate: dayjs('2025-02-12'),
};

export const sampleWithNewData: NewReview = {
  rating: 5,
  reviewDate: dayjs('2025-02-12'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
