import dayjs from 'dayjs/esm';

import { IReservation, NewReservation } from './reservation.model';

export const sampleWithRequiredData: IReservation = {
  id: 19023,
  reservationDate: dayjs('2025-02-13T07:15'),
  expirationDate: dayjs('2025-02-13T09:03'),
  status: 'CONFIRMED',
};

export const sampleWithPartialData: IReservation = {
  id: 8205,
  reservationDate: dayjs('2025-02-13T10:31'),
  expirationDate: dayjs('2025-02-12T19:10'),
  status: 'CONFIRMED',
};

export const sampleWithFullData: IReservation = {
  id: 25988,
  reservationDate: dayjs('2025-02-13T07:59'),
  expirationDate: dayjs('2025-02-13T01:23'),
  status: 'CANCELLED',
};

export const sampleWithNewData: NewReservation = {
  reservationDate: dayjs('2025-02-12T12:42'),
  expirationDate: dayjs('2025-02-12T14:35'),
  status: 'PENDING',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
