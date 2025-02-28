import dayjs from 'dayjs/esm';
import { IUserDetails } from 'app/entities/user-details/user-details.model';

export interface IConversation {
  id: number;
  dateCreated?: dayjs.Dayjs | null;
  userOne?: IUserDetails | null;
  userTwo?: IUserDetails | null;
}

export type NewConversation = Omit<IConversation, 'id'> & { id: null };
