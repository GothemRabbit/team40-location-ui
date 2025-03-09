import dayjs from 'dayjs/esm';
import { IUserDetails } from 'app/entities/user-details/user-details.model';

export interface IConversation {
  id: number;
  dateCreated?: dayjs.Dayjs | null;
  participants?: Pick<IUserDetails, 'id'>[] | null;
}

export type NewConversation = Omit<IConversation, 'id'> & { id: null };
