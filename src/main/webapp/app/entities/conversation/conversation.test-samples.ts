import dayjs from 'dayjs/esm';

import { IConversation, NewConversation } from './conversation.model';

export const sampleWithRequiredData: IConversation = {
  id: 9318,
  dateCreated: dayjs('2025-02-12'),
};

export const sampleWithPartialData: IConversation = {
  id: 13640,
  dateCreated: dayjs('2025-02-13'),
};

export const sampleWithFullData: IConversation = {
  id: 21027,
  dateCreated: dayjs('2025-02-12'),
};

export const sampleWithNewData: NewConversation = {
  dateCreated: dayjs('2025-02-12'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
