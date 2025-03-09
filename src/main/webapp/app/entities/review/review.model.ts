import dayjs from 'dayjs/esm';
import { IUserDetails } from 'app/entities/user-details/user-details.model';

export interface IReview {
  id: number;
  rating?: number | null;
  comments?: string | null;
  date?: dayjs.Dayjs | null;
  buyer?: Pick<IUserDetails, 'id'> | null;
  seller?: Pick<IUserDetails, 'id'> | null;
}

export type NewReview = Omit<IReview, 'id'> & { id: null };
