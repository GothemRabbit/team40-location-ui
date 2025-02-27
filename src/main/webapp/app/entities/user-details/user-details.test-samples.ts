import dayjs from 'dayjs/esm';

import { IUserDetails, NewUserDetails } from './user-details.model';

export const sampleWithRequiredData: IUserDetails = {
  id: 1900,
  userName: 'ill like',
  firstName: 'Demetris',
  lastName: 'Barton',
  email: 'Micah66@gmail.com',
  phoneNumber: 'instead beneath',
  rating: 2.6,
  address: 'loyally',
};

export const sampleWithPartialData: IUserDetails = {
  id: 19293,
  bioImage: '../fake-data/blob/hipster.png',
  bioImageContentType: 'unknown',
  userName: 'jive gee splash',
  firstName: 'Alvis',
  lastName: 'Cartwright',
  gender: 'OTHER',
  birthDate: dayjs('2025-02-12'),
  email: 'Shaylee.Jerde@yahoo.com',
  phoneNumber: 'devise mount',
  preferences: 'eek owlishly making',
  rating: 3.83,
  address: 'midst harvest reorient',
};

export const sampleWithFullData: IUserDetails = {
  id: 19340,
  bioImage: '../fake-data/blob/hipster.png',
  bioImageContentType: 'unknown',
  userName: 'lucky',
  firstName: 'Autumn',
  lastName: 'Homenick',
  gender: 'FEMALE',
  birthDate: dayjs('2025-02-13'),
  email: 'Brando20@hotmail.com',
  phoneNumber: 'privilege the when',
  preferences: 'rigidly unlike dependency',
  rating: 2.49,
  address: 'always',
};

export const sampleWithNewData: NewUserDetails = {
  userName: 'fiercely',
  firstName: 'Cora',
  lastName: 'Mosciski',
  email: 'Bartholome81@yahoo.com',
  phoneNumber: 'ick provided queasily',
  rating: 4.98,
  address: 'pointless boohoo',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
