import dayjs from 'dayjs/esm';

import { IUserSearchHistory, NewUserSearchHistory } from './user-search-history.model';

export const sampleWithRequiredData: IUserSearchHistory = {
  id: 1402,
  searchQuery: 'till ha',
  searchDate: dayjs('2025-02-27T15:15'),
};

export const sampleWithPartialData: IUserSearchHistory = {
  id: 19496,
  searchQuery: 'jubilantly',
  filters: 'woot',
  searchDate: dayjs('2025-02-27T12:29'),
};

export const sampleWithFullData: IUserSearchHistory = {
  id: 27435,
  searchQuery: 'lazily the blah',
  filters: 'ugh noted instead',
  searchDate: dayjs('2025-02-26T22:43'),
};

export const sampleWithNewData: NewUserSearchHistory = {
  searchQuery: 'provided',
  searchDate: dayjs('2025-02-27T07:31'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
