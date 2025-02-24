import dayjs from 'dayjs/esm';

import { IConversation, NewConversation } from './conversation.model';

export const sampleWithRequiredData: IConversation = {
  id: 2161,
  dateCreated: dayjs('2025-02-13'),
};

export const sampleWithPartialData: IConversation = {
  id: 32149,
  dateCreated: dayjs('2025-02-13'),
};

export const sampleWithFullData: IConversation = {
  id: 16535,
  dateCreated: dayjs('2025-02-13'),
};

export const sampleWithNewData: NewConversation = {
  dateCreated: dayjs('2025-02-12'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
