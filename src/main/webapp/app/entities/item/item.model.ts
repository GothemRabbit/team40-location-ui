import dayjs from 'dayjs/esm';
import { IWishlist } from 'app/entities/wishlist/wishlist.model';
import { IProfileDetails } from 'app/entities/profile-details/profile-details.model';
import { IUserDetails } from 'app/entities/user-details/user-details.model';
import { Condition } from 'app/entities/enumerations/condition.model';
import { Category } from 'app/entities/enumerations/category.model';
import { IImages } from '../images/images.model';
import { ILikes } from '../likes/likes.model';
import { IProductStatus } from '../product-status/product-status.model';

export interface IItem {
  id: number;
  title?: string | null;
  price?: number | null;
  condition?: keyof typeof Condition | null;
  category?: keyof typeof Category | null;
  description?: string | null;
  sizeItem?: string | null;
  brand?: string | null;
  colour?: string | null;
  timeListed?: dayjs.Dayjs | null;
  wishlists?: Pick<IWishlist, 'id'>[] | null;
  profileDetails?: IProfileDetails | null;
  seller?: Pick<IUserDetails, 'id'> | null;
  images?: IImages[];
  imageUrl?: string;
  likes?: ILikes[];
  likesCount?: number;
  isLikedByUser?: boolean;
  status?: IProductStatus | null;
}

export type NewItem = Omit<IItem, 'id'> & { id: null };
