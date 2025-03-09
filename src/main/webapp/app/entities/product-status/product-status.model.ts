import dayjs from 'dayjs/esm';
import { IItem } from 'app/entities/item/item.model';
import { IConversation } from 'app/entities/conversation/conversation.model';
import { IUserDetails } from 'app/entities/user-details/user-details.model';
import { ILocation } from 'app/entities/location/location.model';
import { ProductState } from 'app/entities/enumerations/product-state.model';

export interface IProductStatus {
  id: number;
  status?: keyof typeof ProductState | null;
  meetingTime?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  createdAt?: dayjs.Dayjs | null;
  item?: Pick<IItem, 'id'> | null;
  conversation?: Pick<IConversation, 'id'> | null;
  buyer?: Pick<IUserDetails, 'id'> | null;
  seller?: Pick<IUserDetails, 'id'> | null;
  meetingLocation?: Pick<ILocation, 'id'> | null;
}

export type NewProductStatus = Omit<IProductStatus, 'id'> & { id: null };
