import dayjs from 'dayjs/esm';

import { IReservation, NewReservation } from './reservation.model';

export const sampleWithRequiredData: IReservation = {
  id: 18062,
  reservationDate: dayjs('2025-02-13T06:17'),
  expirationDate: dayjs('2025-02-13T08:51'),
  status: 'CANCELLED',
};

export const sampleWithPartialData: IReservation = {
  id: 11305,
  reservationDate: dayjs('2025-02-13T03:54'),
  expirationDate: dayjs('2025-02-13T06:36'),
  status: 'CONFIRMED',
};

export const sampleWithFullData: IReservation = {
  id: 31395,
  reservationDate: dayjs('2025-02-13T08:20'),
  expirationDate: dayjs('2025-02-12T23:27'),
  status: 'PENDING',
};

export const sampleWithNewData: NewReservation = {
  reservationDate: dayjs('2025-02-13T03:48'),
  expirationDate: dayjs('2025-02-13T05:03'),
  status: 'CONFIRMED',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
