import dayjs from 'dayjs/esm';

import { IUserDetails, NewUserDetails } from './user-details.model';

export const sampleWithRequiredData: IUserDetails = {
  id: 9384,
  userName: '_r<2@',
};

export const sampleWithPartialData: IUserDetails = {
  id: 17026,
  userName: 'BE-`&:',
  birthDate: dayjs('2025-02-12'),
};

export const sampleWithFullData: IUserDetails = {
  id: 15260,
  bioImage: '../fake-data/blob/hipster.png',
  bioImageContentType: 'unknown',
  userName: 'x',
  birthDate: dayjs('2025-02-12'),
};

export const sampleWithNewData: NewUserDetails = {
  userName: '~E/^',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
