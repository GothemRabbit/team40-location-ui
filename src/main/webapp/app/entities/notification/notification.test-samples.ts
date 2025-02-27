import dayjs from 'dayjs/esm';

import { INotification, NewNotification } from './notification.model';

export const sampleWithRequiredData: INotification = {
  id: 18156,
  title: 'unlined prickly',
  message: '../fake-data/blob/hipster.txt',
  notificationType: 'REVIEW',
  isRead: false,
  createdAt: dayjs('2025-02-24T00:44'),
};

export const sampleWithPartialData: INotification = {
  id: 14180,
  title: 'loyally degrease incidentally',
  message: '../fake-data/blob/hipster.txt',
  notificationType: 'STATUS_CHANGE',
  isRead: false,
  createdAt: dayjs('2025-02-24T05:33'),
};

export const sampleWithFullData: INotification = {
  id: 25107,
  title: 'fail regulate',
  message: '../fake-data/blob/hipster.txt',
  notificationType: 'MESSAGE',
  isRead: true,
  createdAt: dayjs('2025-02-23T23:48'),
};

export const sampleWithNewData: NewNotification = {
  title: 'um huzzah',
  message: '../fake-data/blob/hipster.txt',
  notificationType: 'REVIEW',
  isRead: false,
  createdAt: dayjs('2025-02-24T05:05'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
