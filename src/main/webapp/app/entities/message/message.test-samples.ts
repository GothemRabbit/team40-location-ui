import dayjs from 'dayjs/esm';

import { IMessage, NewMessage } from './message.model';

export const sampleWithRequiredData: IMessage = {
  id: 9224,
  content: '../fake-data/blob/hipster.txt',
  timestamp: dayjs('2025-02-12T19:52'),
  isRead: false,
};

export const sampleWithPartialData: IMessage = {
  id: 13817,
  content: '../fake-data/blob/hipster.txt',
  timestamp: dayjs('2025-02-12T21:30'),
  isRead: true,
};

export const sampleWithFullData: IMessage = {
  id: 21769,
  content: '../fake-data/blob/hipster.txt',
  timestamp: dayjs('2025-02-12T21:35'),
  isRead: true,
};

export const sampleWithNewData: NewMessage = {
  content: '../fake-data/blob/hipster.txt',
  timestamp: dayjs('2025-02-12T23:58'),
  isRead: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
