import dayjs from 'dayjs/esm';

import { IUserInteraction, NewUserInteraction } from './user-interaction.model';

export const sampleWithRequiredData: IUserInteraction = {
  id: 16723,
  type: 'RECOMMENDATION',
  interactionDate: dayjs('2025-02-13T06:14'),
};

export const sampleWithPartialData: IUserInteraction = {
  id: 32599,
  type: 'RECOMMENDATION',
  interactionDate: dayjs('2025-02-12T18:37'),
};

export const sampleWithFullData: IUserInteraction = {
  id: 24999,
  type: 'RECOMMENDATION',
  details: 'while now',
  interactionDate: dayjs('2025-02-13T08:36'),
};

export const sampleWithNewData: NewUserInteraction = {
  type: 'SEARCH',
  interactionDate: dayjs('2025-02-13T07:51'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
