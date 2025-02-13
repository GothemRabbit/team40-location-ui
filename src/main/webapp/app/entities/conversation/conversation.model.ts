import dayjs from 'dayjs/esm';

export interface IConversation {
  id: number;
  dateCreated?: dayjs.Dayjs | null;
}

export type NewConversation = Omit<IConversation, 'id'> & { id: null };
