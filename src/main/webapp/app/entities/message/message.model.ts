import dayjs from 'dayjs/esm';
import { IConversation } from 'app/entities/conversation/conversation.model';
import { IUserDetails } from 'app/entities/user-details/user-details.model';

export interface IMessage {
  id: number;
  content?: string | null;
  timestamp?: dayjs.Dayjs | null;
  isRead?: boolean | null;
  convo?: IConversation | null;
  sender?: IUserDetails | null;
  receiver?: IUserDetails | null;
}

export type NewMessage = Omit<IMessage, 'id'> & { id: null };
