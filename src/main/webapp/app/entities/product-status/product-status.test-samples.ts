import dayjs from 'dayjs/esm';

import { IProductStatus, NewProductStatus } from './product-status.model';

export const sampleWithRequiredData: IProductStatus = {
  id: 25879,
  status: 'PENDING',
};

export const sampleWithPartialData: IProductStatus = {
  id: 31202,
  status: 'PENDING',
  meetingTime: dayjs('2025-02-13T10:37'),
  createdAt: dayjs('2025-02-12T17:50'),
};

export const sampleWithFullData: IProductStatus = {
  id: 13958,
  status: 'RESERVED',
  meetingTime: dayjs('2025-02-13T11:21'),
  updatedAt: dayjs('2025-02-13T10:19'),
  createdAt: dayjs('2025-02-12T23:06'),
};

export const sampleWithNewData: NewProductStatus = {
  status: 'RESERVED',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
