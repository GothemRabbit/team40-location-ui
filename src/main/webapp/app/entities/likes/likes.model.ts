import { IItem } from 'app/entities/item/item.model';
import { IUserDetails } from 'app/entities/user-details/user-details.model';

export interface ILikes {
  id: number;
  liked?: boolean | null;
  item?: Pick<IItem, 'id'> | null;
  user?: Pick<IUserDetails, 'id'> | null;
}

export type NewLikes = Omit<ILikes, 'id'> & { id: null };
