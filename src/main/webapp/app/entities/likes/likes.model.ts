import { IItem } from 'app/entities/item/item.model';
import { IProfileDetails } from 'app/entities/profile-details/profile-details.model';

export interface ILikes {
  id: number;
  liked?: boolean | null;
  item?: Pick<IItem, 'id'> | null;
  profileDetails?: Pick<IProfileDetails, 'id'> | null;
}

export type NewLikes = Omit<ILikes, 'id'> & { id: null };
