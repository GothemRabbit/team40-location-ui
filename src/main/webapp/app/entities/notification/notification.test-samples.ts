import dayjs from 'dayjs/esm';

import { INotification, NewNotification } from './notification.model';

export const sampleWithRequiredData: INotification = {
  id: 23113,
  title: 'jubilantly browse steel',
  message: '../fake-data/blob/hipster.txt',
  notificationType: 'STATUS_CHANGE',
  isRead: true,
  createdAt: dayjs('2025-02-24T12:10'),
};

export const sampleWithPartialData: INotification = {
  id: 8655,
  title: 'generously unpleasant uh-huh',
  message: '../fake-data/blob/hipster.txt',
  notificationType: 'MESSAGE',
  isRead: true,
  createdAt: dayjs('2025-02-24T02:06'),
};

export const sampleWithFullData: INotification = {
  id: 30535,
  title: 'while',
  message: '../fake-data/blob/hipster.txt',
  notificationType: 'REVIEW',
  isRead: true,
  createdAt: dayjs('2025-02-23T21:25'),
};

export const sampleWithNewData: NewNotification = {
  title: 'founder',
  message: '../fake-data/blob/hipster.txt',
  notificationType: 'MESSAGE',
  isRead: true,
  createdAt: dayjs('2025-02-24T07:33'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
