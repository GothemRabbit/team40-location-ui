import dayjs from 'dayjs/esm';
import { IProfileDetails } from 'app/entities/profile-details/profile-details.model';
import { IUserDetails } from 'app/entities/user-details/user-details.model';

export interface IConversation {
  id: number;
  dateCreated?: dayjs.Dayjs | null;
  profileDetails?: Pick<IProfileDetails, 'id'>[] | null;
  participants?: Pick<IUserDetails, 'id'>[] | null;
}

export type NewConversation = Omit<IConversation, 'id'> & { id: null };
