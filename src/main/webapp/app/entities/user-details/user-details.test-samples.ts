import dayjs from 'dayjs/esm';

import { IUserDetails, NewUserDetails } from './user-details.model';

export const sampleWithRequiredData: IUserDetails = {
  id: 11078,
  userName: 'enhance wherever fluff',
  firstName: 'Destini',
  lastName: 'Kerluke',
  email: 'Icie.Kuvalis@yahoo.com',
  phoneNumber: 'arrange',
  rating: 2.06,
  address: 'shy humidity when',
};

export const sampleWithPartialData: IUserDetails = {
  id: 25625,
  userName: 'through',
  firstName: 'Bridget',
  lastName: 'Hermann',
  gender: 'OTHER',
  birthDate: dayjs('2025-02-13'),
  email: 'Pascale70@hotmail.com',
  phoneNumber: 'lest briefly round',
  rating: 4.15,
  address: 'whoever',
};

export const sampleWithFullData: IUserDetails = {
  id: 26070,
  bioImage: '../fake-data/blob/hipster.png',
  bioImageContentType: 'unknown',
  userName: 'far',
  firstName: 'Emil',
  lastName: 'Senger',
  gender: 'MALE',
  birthDate: dayjs('2025-02-13'),
  email: 'Jarrett96@gmail.com',
  phoneNumber: 'for',
  preferences: 'weight',
  rating: 2.01,
  address: 'mundane duh',
};

export const sampleWithNewData: NewUserDetails = {
  userName: 'discourse properly',
  firstName: 'Gavin',
  lastName: 'Kozey',
  email: 'Seamus_Romaguera91@hotmail.com',
  phoneNumber: 'smart jaggedly noteworthy',
  rating: 4.63,
  address: 'yuck',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
