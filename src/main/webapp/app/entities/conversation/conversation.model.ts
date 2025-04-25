import dayjs from 'dayjs/esm';
import { IProfileDetails } from 'app/entities/profile-details/profile-details.model';

export interface IConversation {
  id: number;
  dateCreated?: dayjs.Dayjs | null;
  profileDetails?: Pick<IProfileDetails, 'id'>[] | null;
  participants?: Pick<IProfileDetails, 'id'>[] | null;
}

export type NewConversation = Omit<IConversation, 'id'> & { id: null };
