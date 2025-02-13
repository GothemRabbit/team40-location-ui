import dayjs from 'dayjs/esm';
import { ReservationStatus } from 'app/entities/enumerations/reservation-status.model';

export interface IReservation {
  id: number;
  reservationDate?: dayjs.Dayjs | null;
  expirationDate?: dayjs.Dayjs | null;
  status?: keyof typeof ReservationStatus | null;
}

export type NewReservation = Omit<IReservation, 'id'> & { id: null };
