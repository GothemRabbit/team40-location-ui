import dayjs from 'dayjs/esm';

import { IMessage, NewMessage } from './message.model';

export const sampleWithRequiredData: IMessage = {
  id: 3266,
  content: '../fake-data/blob/hipster.txt',
  timestamp: dayjs('2025-02-13T00:14'),
  isRead: true,
};

export const sampleWithPartialData: IMessage = {
  id: 31532,
  content: '../fake-data/blob/hipster.txt',
  timestamp: dayjs('2025-02-12T21:35'),
  isRead: false,
};

export const sampleWithFullData: IMessage = {
  id: 4996,
  content: '../fake-data/blob/hipster.txt',
  timestamp: dayjs('2025-02-13T01:28'),
  isRead: true,
};

export const sampleWithNewData: NewMessage = {
  content: '../fake-data/blob/hipster.txt',
  timestamp: dayjs('2025-02-13T05:02'),
  isRead: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
