import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { ILocation } from 'app/entities/location/location.model';
import { IConversation } from 'app/entities/conversation/conversation.model';

export interface IUserDetails {
  id: number;
  bioImage?: string | null;
  bioImageContentType?: string | null;
  userName?: string | null;
  birthDate?: dayjs.Dayjs | null;
  user?: Pick<IUser, 'id'> | null;
  meetupLocations?: Pick<ILocation, 'id'>[] | null;
  chats?: Pick<IConversation, 'id'>[] | null;
}

export type NewUserDetails = Omit<IUserDetails, 'id'> & { id: null };
