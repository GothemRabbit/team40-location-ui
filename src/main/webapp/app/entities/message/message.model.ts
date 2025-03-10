import dayjs from 'dayjs/esm';
import { IConversation } from 'app/entities/conversation/conversation.model';
import { IProfileDetails } from 'app/entities/profile-details/profile-details.model';

export interface IMessage {
  id: number;
  content?: string | null;
  timestamp?: dayjs.Dayjs | null;
  isRead?: boolean | null;
  conversation?: Pick<IConversation, 'id'> | null;
  profileDetails?: Pick<IProfileDetails, 'id'> | null;
}

export type NewMessage = Omit<IMessage, 'id'> & { id: null };
