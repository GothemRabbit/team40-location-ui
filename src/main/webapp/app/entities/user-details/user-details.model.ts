import dayjs from 'dayjs/esm';
import { Gender } from 'app/entities/enumerations/gender.model';

export interface IUserDetails {
  id: number;
  bioImage?: string | null;
  bioImageContentType?: string | null;
  userName?: string | null;
  firstName?: string | null;
  lastName?: string | null;
  gender?: keyof typeof Gender | null;
  birthDate?: dayjs.Dayjs | null;
  email?: string | null;
  phoneNumber?: string | null;
  preferences?: string | null;
  rating?: number | null;
  address?: string | null;
}

export type NewUserDetails = Omit<IUserDetails, 'id'> & { id: null };
