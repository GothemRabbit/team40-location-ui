import { IUserDetails } from 'app/entities/user-details/user-details.model';

export interface ILocation {
  id: number;
  address?: string | null;
  latitude?: number | null;
  longitude?: number | null;
  postcode?: string | null;
  users?: Pick<IUserDetails, 'id'>[] | null;
}

export type NewLocation = Omit<ILocation, 'id'> & { id: null };
