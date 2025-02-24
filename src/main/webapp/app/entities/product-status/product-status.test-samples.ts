import dayjs from 'dayjs/esm';

import { IProductStatus, NewProductStatus } from './product-status.model';

export const sampleWithRequiredData: IProductStatus = {
  id: 28150,
  status: 'PENDING',
  createdAt: dayjs('2025-02-12T14:46'),
};

export const sampleWithPartialData: IProductStatus = {
  id: 27261,
  status: 'AVAILABLE',
  meetingTime: dayjs('2025-02-13T04:31'),
  createdAt: dayjs('2025-02-13T02:38'),
  updatedAt: dayjs('2025-02-13T09:49'),
};

export const sampleWithFullData: IProductStatus = {
  id: 25440,
  status: 'PENDING',
  meetingTime: dayjs('2025-02-13T00:32'),
  meetingLocation: 'animated',
  chatLink: 'somber aside within',
  createdAt: dayjs('2025-02-13T04:36'),
  updatedAt: dayjs('2025-02-13T10:36'),
};

export const sampleWithNewData: NewProductStatus = {
  status: 'CANCELLED',
  createdAt: dayjs('2025-02-12T19:19'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
