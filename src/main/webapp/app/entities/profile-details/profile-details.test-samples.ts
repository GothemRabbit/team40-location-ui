import dayjs from 'dayjs/esm';

import { IProfileDetails, NewProfileDetails } from './profile-details.model';

export const sampleWithRequiredData: IProfileDetails = {
  id: 31997,
  userName: "/P'?QU",
};

export const sampleWithPartialData: IProfileDetails = {
  id: 26285,
  bioImage: '../fake-data/blob/hipster.png',
  bioImageContentType: 'unknown',
  userName: '2&{;-',
  birthDate: dayjs('2025-03-08'),
};

export const sampleWithFullData: IProfileDetails = {
  id: 21312,
  bioImage: '../fake-data/blob/hipster.png',
  bioImageContentType: 'unknown',
  userName: '5',
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
