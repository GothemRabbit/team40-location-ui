import { IProfileDetails } from 'app/entities/profile-details/profile-details.model';
import { IUserDetails } from 'app/entities/user-details/user-details.model';
import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LocationComponent } from './list/location.component';

@NgModule({
  declarations: [],
  imports: [CommonModule, LocationComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA], // ✅ 加在这里
})
export class LocationModule {}
export interface ILocation {
  id: number;
  address?: string | null;
  latitude?: number | null;
  longitude?: number | null;
  postcode?: string | null;
  profileDetails?: Pick<IProfileDetails, 'id'>[] | null;
  users?: Pick<IUserDetails, 'id'>[] | null;
}

export type NewLocation = Omit<ILocation, 'id'> & { id: null };
