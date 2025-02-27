import dayjs from 'dayjs/esm';

import { IMessage, NewMessage } from './message.model';

export const sampleWithRequiredData: IMessage = {
  id: 20606,
  content: '../fake-data/blob/hipster.txt',
  timestamp: dayjs('2025-02-13T07:06'),
};

export const sampleWithPartialData: IMessage = {
  id: 15608,
  content: '../fake-data/blob/hipster.txt',
  timestamp: dayjs('2025-02-13T07:37'),
};

export const sampleWithFullData: IMessage = {
  id: 20162,
  content: '../fake-data/blob/hipster.txt',
  timestamp: dayjs('2025-02-13T03:37'),
};

export const sampleWithNewData: NewMessage = {
  content: '../fake-data/blob/hipster.txt',
  timestamp: dayjs('2025-02-12T14:18'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
