import dayjs from 'dayjs/esm';

import { IUserInteraction, NewUserInteraction } from './user-interaction.model';

export const sampleWithRequiredData: IUserInteraction = {
  id: 17322,
  type: 'SEARCH',
  interactionDate: dayjs('2025-02-12T22:29'),
};

export const sampleWithPartialData: IUserInteraction = {
  id: 25589,
  type: 'SEARCH',
  details: 'husky schlep behind',
  interactionDate: dayjs('2025-02-13T12:47'),
};

export const sampleWithFullData: IUserInteraction = {
  id: 2270,
  type: 'SEARCH',
  details: 'although bitterly gorgeous',
  interactionDate: dayjs('2025-02-12T15:25'),
};

export const sampleWithNewData: NewUserInteraction = {
  type: 'RECOMMENDATION',
  interactionDate: dayjs('2025-02-12T23:27'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
