import dayjs from 'dayjs/esm';

export interface IUserSearchHistory {
  id: number;
  searchQuery?: string | null;
  filters?: string | null;
  searchDate?: dayjs.Dayjs | null;
}

export type NewUserSearchHistory = Omit<IUserSearchHistory, 'id'> & { id: null };
