import dayjs from 'dayjs/esm';

import { IConversation, NewConversation } from './conversation.model';

export const sampleWithRequiredData: IConversation = {
  id: 28530,
  dateCreated: dayjs('2025-02-13T03:29'),
};

export const sampleWithPartialData: IConversation = {
  id: 8068,
  dateCreated: dayjs('2025-02-12T14:33'),
};

export const sampleWithFullData: IConversation = {
  id: 19689,
  dateCreated: dayjs('2025-02-12T16:57'),
};

export const sampleWithNewData: NewConversation = {
  dateCreated: dayjs('2025-02-13T06:44'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
