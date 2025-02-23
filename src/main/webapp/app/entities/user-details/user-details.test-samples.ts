import dayjs from 'dayjs/esm';

import { IUserDetails, NewUserDetails } from './user-details.model';

export const sampleWithRequiredData: IUserDetails = {
  id: 29008,
  userName: 'toaster',
  firstName: 'Trevion',
  lastName: 'Hammes',
  email: 'Sylvia_Hegmann@gmail.com',
  phoneNumber: 'print',
  rating: 2.35,
  address: 'responsible mmm',
};

export const sampleWithPartialData: IUserDetails = {
  id: 22544,
  userName: 'instantly',
  firstName: 'Shaun',
  lastName: 'Gottlieb',
  email: 'Kyleigh.Tromp@gmail.com',
  phoneNumber: 'opera',
  rating: 2.09,
  address: 'exaggerate buzzing',
};

export const sampleWithFullData: IUserDetails = {
  id: 10610,
  bioImage: '../fake-data/blob/hipster.png',
  bioImageContentType: 'unknown',
  userName: 'impressive neighboring',
  firstName: 'Hertha',
  lastName: 'Kunze',
  gender: 'FEMALE',
  birthDate: dayjs('2025-02-13'),
  email: 'Kristoffer.Dietrich39@yahoo.com',
  phoneNumber: 'what diligently',
  preferences: 'whose surface redraw',
  rating: 3.97,
  address: 'drat athletic fooey',
};

export const sampleWithNewData: NewUserDetails = {
  userName: 'solemnly unexpectedly pant',
  firstName: 'Kendrick',
  lastName: 'Armstrong-Johnson',
  email: 'Boyd_Vandervort44@yahoo.com',
  phoneNumber: 'eek dispose per',
  rating: 4.8,
  address: 'gee energetic',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
