import dayjs from 'dayjs/esm';

import { IConversation, NewConversation } from './conversation.model';

export const sampleWithRequiredData: IConversation = {
  id: 11905,
  dateCreated: dayjs('2025-02-09'),
};

export const sampleWithPartialData: IConversation = {
  id: 28318,
  dateCreated: dayjs('2025-02-09'),
};

export const sampleWithFullData: IConversation = {
  id: 6061,
  dateCreated: dayjs('2025-02-09'),
};

export const sampleWithNewData: NewConversation = {
  dateCreated: dayjs('2025-02-09'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
