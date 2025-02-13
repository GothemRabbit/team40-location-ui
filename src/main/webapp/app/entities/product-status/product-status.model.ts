import dayjs from 'dayjs/esm';
import { ProductState } from 'app/entities/enumerations/product-state.model';

export interface IProductStatus {
  id: number;
  status?: keyof typeof ProductState | null;
  meetingTime?: dayjs.Dayjs | null;
  meetingLocation?: string | null;
  chatLink?: string | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
}

export type NewProductStatus = Omit<IProductStatus, 'id'> & { id: null };
