import dayjs from 'dayjs/esm';

import { IProductStatus, NewProductStatus } from './product-status.model';

export const sampleWithRequiredData: IProductStatus = {
  id: 15539,
  status: 'PENDING',
};

export const sampleWithPartialData: IProductStatus = {
  id: 14600,
  status: 'REJECTED',
  meetingTime: dayjs('2025-02-13T04:04'),
};

export const sampleWithFullData: IProductStatus = {
  id: 25090,
  status: 'REJECTED',
  meetingTime: dayjs('2025-02-13T06:49'),
  updatedAt: dayjs('2025-02-12T14:29'),
  createdAt: dayjs('2025-02-13T02:27'),
};

export const sampleWithNewData: NewProductStatus = {
  status: 'REJECTED',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
