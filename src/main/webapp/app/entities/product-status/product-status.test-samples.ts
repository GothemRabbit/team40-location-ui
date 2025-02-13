import dayjs from 'dayjs/esm';

import { IProductStatus, NewProductStatus } from './product-status.model';

export const sampleWithRequiredData: IProductStatus = {
  id: 2369,
  status: 'RESERVED',
  createdAt: dayjs('2025-02-13T08:36'),
};

export const sampleWithPartialData: IProductStatus = {
  id: 24477,
  status: 'AVAILABLE',
  meetingLocation: 'swat sleepily',
  createdAt: dayjs('2025-02-12T14:14'),
};

export const sampleWithFullData: IProductStatus = {
  id: 7293,
  status: 'RESERVED',
  meetingTime: dayjs('2025-02-12T14:26'),
  meetingLocation: 'ad',
  chatLink: 'shred masticate print',
  createdAt: dayjs('2025-02-13T00:33'),
  updatedAt: dayjs('2025-02-12T20:10'),
};

export const sampleWithNewData: NewProductStatus = {
  status: 'CANCELLED',
  createdAt: dayjs('2025-02-13T06:13'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
