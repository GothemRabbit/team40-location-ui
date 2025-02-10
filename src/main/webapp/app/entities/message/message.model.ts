import dayjs from 'dayjs/esm';
import { IUserDetails } from 'app/entities/user-details/user-details.model';
import { IConversation } from 'app/entities/conversation/conversation.model';

export interface IMessage {
  id: number;
  content?: string | null;
  timestamp?: dayjs.Dayjs | null;
  userDetails?: IUserDetails | null;
  conversation?: IConversation | null;
}

export type NewMessage = Omit<IMessage, 'id'> & { id: null };
