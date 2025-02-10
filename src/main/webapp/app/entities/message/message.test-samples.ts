import dayjs from 'dayjs/esm';

import { IMessage, NewMessage } from './message.model';

export const sampleWithRequiredData: IMessage = {
  id: 3285,
  content: '../fake-data/blob/hipster.txt',
  timestamp: dayjs('2025-02-09T19:40'),
};

export const sampleWithPartialData: IMessage = {
  id: 10661,
  content: '../fake-data/blob/hipster.txt',
  timestamp: dayjs('2025-02-09T17:48'),
};

export const sampleWithFullData: IMessage = {
  id: 21695,
  content: '../fake-data/blob/hipster.txt',
  timestamp: dayjs('2025-02-09T17:57'),
};

export const sampleWithNewData: NewMessage = {
  content: '../fake-data/blob/hipster.txt',
  timestamp: dayjs('2025-02-09T10:29'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
