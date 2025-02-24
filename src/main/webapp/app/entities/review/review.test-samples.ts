import dayjs from 'dayjs/esm';

import { IReview, NewReview } from './review.model';

export const sampleWithRequiredData: IReview = {
  id: 25871,
  rating: 2.74,
  reviewDate: dayjs('2025-02-13'),
};

export const sampleWithPartialData: IReview = {
  id: 5767,
  rating: 1.86,
  reviewDate: dayjs('2025-02-13'),
};

export const sampleWithFullData: IReview = {
  id: 5612,
  rating: 3.37,
  reviewText: '../fake-data/blob/hipster.txt',
  reviewDate: dayjs('2025-02-12'),
};

export const sampleWithNewData: NewReview = {
  rating: 3.48,
  reviewDate: dayjs('2025-02-13'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
