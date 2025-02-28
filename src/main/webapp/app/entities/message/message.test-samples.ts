import dayjs from 'dayjs/esm';

import { IMessage, NewMessage } from './message.model';

export const sampleWithRequiredData: IMessage = {
  id: 25975,
  content: '../fake-data/blob/hipster.txt',
  timestamp: dayjs('2025-02-12T23:31'),
  isRead: false,
};

export const sampleWithPartialData: IMessage = {
  id: 20162,
  content: '../fake-data/blob/hipster.txt',
  timestamp: dayjs('2025-02-13T03:37'),
  isRead: true,
};

export const sampleWithFullData: IMessage = {
  id: 8705,
  content: '../fake-data/blob/hipster.txt',
  timestamp: dayjs('2025-02-12T19:59'),
  isRead: true,
};

export const sampleWithNewData: NewMessage = {
  content: '../fake-data/blob/hipster.txt',
  timestamp: dayjs('2025-02-13T03:59'),
  isRead: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
