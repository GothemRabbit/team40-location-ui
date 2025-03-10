import dayjs from 'dayjs/esm';
import { IProfileDetails } from 'app/entities/profile-details/profile-details.model';
import { IUserDetails } from 'app/entities/user-details/user-details.model';

export interface IReview {
  id: number;
  rating?: number | null;
  comments?: string | null;
  date?: dayjs.Dayjs | null;
  profileDetails?: Pick<IProfileDetails, 'id'> | null;
  buyer?: Pick<IUserDetails, 'id'> | null;
  seller?: Pick<IUserDetails, 'id'> | null;
}

export type NewReview = Omit<IReview, 'id'> & { id: null };
