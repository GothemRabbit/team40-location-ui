import dayjs from 'dayjs/esm';

import { IProductStatus, NewProductStatus } from './product-status.model';

export const sampleWithRequiredData: IProductStatus = {
  id: 19818,
  status: 'PENDING',
  createdAt: dayjs('2025-02-13T04:53'),
};

export const sampleWithPartialData: IProductStatus = {
  id: 30799,
  status: 'CANCELLED',
  chatLink: 'aboard since via',
  createdAt: dayjs('2025-02-12T13:28'),
  updatedAt: dayjs('2025-02-12T21:35'),
};

export const sampleWithFullData: IProductStatus = {
  id: 18619,
  status: 'COMPLETED',
  meetingTime: dayjs('2025-02-12T21:01'),
  meetingLocation: 'silver unimpressively annual',
  chatLink: 'thongs general',
  createdAt: dayjs('2025-02-13T06:40'),
  updatedAt: dayjs('2025-02-13T04:35'),
};

export const sampleWithNewData: NewProductStatus = {
  status: 'CANCELLED',
  createdAt: dayjs('2025-02-12T13:50'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
