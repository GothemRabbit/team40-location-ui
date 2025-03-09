import dayjs from 'dayjs/esm';

import { IConversation, NewConversation } from './conversation.model';

export const sampleWithRequiredData: IConversation = {
  id: 10583,
  dateCreated: dayjs('2025-02-13T05:28'),
};

export const sampleWithPartialData: IConversation = {
  id: 21370,
  dateCreated: dayjs('2025-02-13T02:21'),
};

export const sampleWithFullData: IConversation = {
  id: 7216,
  dateCreated: dayjs('2025-02-12T15:59'),
};

export const sampleWithNewData: NewConversation = {
  dateCreated: dayjs('2025-02-13T01:48'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
