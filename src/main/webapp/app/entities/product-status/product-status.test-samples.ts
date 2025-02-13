import dayjs from 'dayjs/esm';

import { IProductStatus, NewProductStatus } from './product-status.model';

export const sampleWithRequiredData: IProductStatus = {
  id: 2369,
  status: 'RESERVED',
  createdAt: dayjs('2025-02-13T09:21'),
};

export const sampleWithPartialData: IProductStatus = {
  id: 24477,
  status: 'AVAILABLE',
  meetingLocation: 'swat sleepily',
  createdAt: dayjs('2025-02-12T14:59'),
};

export const sampleWithFullData: IProductStatus = {
  id: 7293,
  status: 'RESERVED',
  meetingTime: dayjs('2025-02-12T15:11'),
  meetingLocation: 'ad',
  chatLink: 'shred masticate print',
  createdAt: dayjs('2025-02-13T01:18'),
  updatedAt: dayjs('2025-02-12T20:55'),
};

export const sampleWithNewData: NewProductStatus = {
  status: 'CANCELLED',
  createdAt: dayjs('2025-02-13T06:58'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
