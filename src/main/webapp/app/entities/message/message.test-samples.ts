import dayjs from 'dayjs/esm';

import { IMessage, NewMessage } from './message.model';

export const sampleWithRequiredData: IMessage = {
  id: 15233,
  content: '../fake-data/blob/hipster.txt',
  timestamp: dayjs('2025-02-13T00:20'),
};

export const sampleWithPartialData: IMessage = {
  id: 15288,
  content: '../fake-data/blob/hipster.txt',
  timestamp: dayjs('2025-02-13T11:06'),
};

export const sampleWithFullData: IMessage = {
  id: 28581,
  content: '../fake-data/blob/hipster.txt',
  timestamp: dayjs('2025-02-12T23:13'),
};

export const sampleWithNewData: NewMessage = {
  content: '../fake-data/blob/hipster.txt',
  timestamp: dayjs('2025-02-12T18:52'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
