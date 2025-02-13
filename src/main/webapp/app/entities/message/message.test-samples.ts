import dayjs from 'dayjs/esm';

import { IMessage, NewMessage } from './message.model';

export const sampleWithRequiredData: IMessage = {
  id: 23271,
  content: '../fake-data/blob/hipster.txt',
  timestamp: dayjs('2025-02-09T00:15'),
};

export const sampleWithPartialData: IMessage = {
  id: 6168,
  content: '../fake-data/blob/hipster.txt',
  timestamp: dayjs('2025-02-09T18:13'),
};

export const sampleWithFullData: IMessage = {
  id: 8864,
  content: '../fake-data/blob/hipster.txt',
  timestamp: dayjs('2025-02-09T19:26'),
};

export const sampleWithNewData: NewMessage = {
  content: '../fake-data/blob/hipster.txt',
  timestamp: dayjs('2025-02-09T19:29'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
