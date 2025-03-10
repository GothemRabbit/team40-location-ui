import dayjs from 'dayjs/esm';

import { IProfileDetails, NewProfileDetails } from './profile-details.model';

export const sampleWithRequiredData: IProfileDetails = {
  id: 30056,
  userName: '|v/P',
};

export const sampleWithPartialData: IProfileDetails = {
  id: 17047,
  bioImage: '../fake-data/blob/hipster.png',
  bioImageContentType: 'unknown',
  userName: '6-le',
  birthDate: dayjs('2025-03-08'),
};

export const sampleWithFullData: IProfileDetails = {
  id: 1876,
  bioImage: '../fake-data/blob/hipster.png',
  bioImageContentType: 'unknown',
  userName: ';-2^+5',
  birthDate: dayjs('2025-03-08'),
};

export const sampleWithNewData: NewProfileDetails = {
  userName: 'CFB<E6',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
