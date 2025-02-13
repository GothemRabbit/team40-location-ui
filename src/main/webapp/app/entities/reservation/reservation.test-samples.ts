import dayjs from 'dayjs/esm';

import { IReservation, NewReservation } from './reservation.model';

export const sampleWithRequiredData: IReservation = {
  id: 17518,
  reservationDate: dayjs('2025-02-13T10:52'),
  expirationDate: dayjs('2025-02-13T12:29'),
  status: 'PENDING',
};

export const sampleWithPartialData: IReservation = {
  id: 31519,
  reservationDate: dayjs('2025-02-13T11:34'),
  expirationDate: dayjs('2025-02-12T13:30'),
  status: 'CANCELLED',
};

export const sampleWithFullData: IReservation = {
  id: 14605,
  reservationDate: dayjs('2025-02-13T12:01'),
  expirationDate: dayjs('2025-02-12T15:46'),
  status: 'CONFIRMED',
};

export const sampleWithNewData: NewReservation = {
  reservationDate: dayjs('2025-02-12T19:10'),
  expirationDate: dayjs('2025-02-12T21:40'),
  status: 'CONFIRMED',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
