import dayjs from 'dayjs/esm';
import { IWishlist } from 'app/entities/wishlist/wishlist.model';
import { IUserDetails } from 'app/entities/user-details/user-details.model';
import { Condition } from 'app/entities/enumerations/condition.model';
import { Category } from 'app/entities/enumerations/category.model';

export interface IItem {
  id: number;
  title?: string | null;
  price?: number | null;
  condition?: keyof typeof Condition | null;
  category?: keyof typeof Category | null;
  description?: string | null;
  brand?: string | null;
  colour?: string | null;
  timeListed?: dayjs.Dayjs | null;
  wishlists?: Pick<IWishlist, 'id'>[] | null;
  seller?: Pick<IUserDetails, 'id'> | null;
}

export type NewItem = Omit<IItem, 'id'> & { id: null };
