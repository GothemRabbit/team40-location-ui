import dayjs from 'dayjs/esm';

import { IConversation, NewConversation } from './conversation.model';

export const sampleWithRequiredData: IConversation = {
  id: 3290,
  dateCreated: dayjs('2025-02-12T18:52'),
};

export const sampleWithPartialData: IConversation = {
  id: 1739,
  dateCreated: dayjs('2025-02-12T22:52'),
};

export const sampleWithFullData: IConversation = {
  id: 7229,
  dateCreated: dayjs('2025-02-12T13:22'),
};

export const sampleWithNewData: NewConversation = {
  dateCreated: dayjs('2025-02-12T21:31'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
