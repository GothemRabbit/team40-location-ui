import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { ILocation } from 'app/entities/location/location.model';
import { IConversation } from 'app/entities/conversation/conversation.model';
import { IReview } from '../review/review.model';

export interface IProfileDetails {
  id: number;
  bioImage?: string | null;
  bioImageContentType?: string | null;
  userName?: string | null;
  birthDate?: dayjs.Dayjs | null;
  user?: Pick<IUser, 'id' | 'login'> | null;
  locations?: Pick<ILocation, 'id'>[] | null;
  conversations?: Pick<IConversation, 'id'>[] | null;
  reviewReceiveds?: IReview[];
}

export type NewProfileDetails = Omit<IProfileDetails, 'id'> & { id: null };
