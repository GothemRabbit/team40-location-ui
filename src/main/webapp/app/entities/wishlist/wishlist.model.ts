import { IUserDetails } from 'app/entities/user-details/user-details.model';
import { IItem } from 'app/entities/item/item.model';
import { VisibilityType } from 'app/entities/enumerations/visibility-type.model';

export interface IWishlist {
  id: number;
  name?: string | null;
  visibility?: keyof typeof VisibilityType | null;
  userDetails?: Pick<IUserDetails, 'id'> | null;
  items?: Pick<IItem, 'id'>[] | null;
}

export type NewWishlist = Omit<IWishlist, 'id'> & { id: null };
