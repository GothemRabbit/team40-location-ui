import dayjs from 'dayjs/esm';

import { IReservation, NewReservation } from './reservation.model';

export const sampleWithRequiredData: IReservation = {
  id: 17518,
  reservationDate: dayjs('2025-02-13T10:07'),
  expirationDate: dayjs('2025-02-13T11:44'),
  status: 'PENDING',
};

export const sampleWithPartialData: IReservation = {
  id: 31519,
  reservationDate: dayjs('2025-02-13T10:49'),
  expirationDate: dayjs('2025-02-12T12:45'),
  status: 'CANCELLED',
};

export const sampleWithFullData: IReservation = {
  id: 14605,
  reservationDate: dayjs('2025-02-13T11:16'),
  expirationDate: dayjs('2025-02-12T15:01'),
  status: 'CONFIRMED',
};

export const sampleWithNewData: NewReservation = {
  reservationDate: dayjs('2025-02-12T18:25'),
  expirationDate: dayjs('2025-02-12T20:55'),
  status: 'CONFIRMED',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
