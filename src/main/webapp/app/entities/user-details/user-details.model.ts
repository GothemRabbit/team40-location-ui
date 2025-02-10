import dayjs from 'dayjs/esm';
import { IConversation } from 'app/entities/conversation/conversation.model';

export interface IUserDetails {
  id: number;
  firstName?: string | null;
  lastName?: string | null;
  email?: string | null;
  profilePic?: string | null;
  profilePicContentType?: string | null;
  lastActive?: dayjs.Dayjs | null;
  conversations?: IConversation[] | null;
}

export type NewUserDetails = Omit<IUserDetails, 'id'> & { id: null };
