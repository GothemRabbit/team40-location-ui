import dayjs from 'dayjs/esm';

import { IUserDetails, NewUserDetails } from './user-details.model';

export const sampleWithRequiredData: IUserDetails = {
  id: 19075,
  firstName: 'Sandra',
  lastName: 'Gulgowski',
  email: 'Lucius54@gmail.com',
  lastActive: dayjs('2025-02-09T02:52'),
};

export const sampleWithPartialData: IUserDetails = {
  id: 10478,
  firstName: 'Bette',
  lastName: 'Mueller',
  email: 'Tressie_Johnson-Collins89@yahoo.com',
  profilePic: '../fake-data/blob/hipster.png',
  profilePicContentType: 'unknown',
  lastActive: dayjs('2025-02-09T08:55'),
};

export const sampleWithFullData: IUserDetails = {
  id: 9661,
  firstName: 'Triston',
  lastName: 'Braun',
  email: 'Gregory64@hotmail.com',
  profilePic: '../fake-data/blob/hipster.png',
  profilePicContentType: 'unknown',
  lastActive: dayjs('2025-02-09T23:35'),
};

export const sampleWithNewData: NewUserDetails = {
  firstName: 'Ila',
  lastName: 'Schumm',
  email: 'Monroe.Ondricka@gmail.com',
  lastActive: dayjs('2025-02-09T07:44'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
