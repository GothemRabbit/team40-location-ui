import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IProfileDetails } from 'app/entities/profile-details/profile-details.model';
import { ProfileDetailsService } from 'app/entities/profile-details/service/profile-details.service';
import { IUserDetails } from 'app/entities/user-details/user-details.model';
import { UserDetailsService } from 'app/entities/user-details/service/user-details.service';
import { LocationService } from '../service/location.service';
import { ILocation } from '../location.model';
import { LocationFormGroup, LocationFormService } from './location-form.service';

@Component({
  standalone: true,
  selector: 'jhi-location-update',
  templateUrl: './location-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class LocationUpdateComponent implements OnInit {
  isSaving = false;
  location: ILocation | null = null;

  profileDetailsSharedCollection: IProfileDetails[] = [];
  userDetailsSharedCollection: IUserDetails[] = [];

  protected locationService = inject(LocationService);
  protected locationFormService = inject(LocationFormService);
  protected profileDetailsService = inject(ProfileDetailsService);
  protected userDetailsService = inject(UserDetailsService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: LocationFormGroup = this.locationFormService.createLocationFormGroup();

  compareProfileDetails = (o1: IProfileDetails | null, o2: IProfileDetails | null): boolean =>
    this.profileDetailsService.compareProfileDetails(o1, o2);

  compareUserDetails = (o1: IUserDetails | null, o2: IUserDetails | null): boolean => this.userDetailsService.compareUserDetails(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ location }) => {
      this.location = location;
      if (location) {
        this.updateForm(location);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const location = this.locationFormService.getLocation(this.editForm);
    if (location.id !== null) {
      this.subscribeToSaveResponse(this.locationService.update(location));
    } else {
      this.subscribeToSaveResponse(this.locationService.create(location));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILocation>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(location: ILocation): void {
    this.location = location;
    this.locationFormService.resetForm(this.editForm, location);

    this.profileDetailsSharedCollection = this.profileDetailsService.addProfileDetailsToCollectionIfMissing<IProfileDetails>(
      this.profileDetailsSharedCollection,
      ...(location.profileDetails ?? []),
    );
    this.userDetailsSharedCollection = this.userDetailsService.addUserDetailsToCollectionIfMissing<IUserDetails>(
      this.userDetailsSharedCollection,
      ...(location.users ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.profileDetailsService
      .query()
      .pipe(map((res: HttpResponse<IProfileDetails[]>) => res.body ?? []))
      .pipe(
        map((profileDetails: IProfileDetails[]) =>
          this.profileDetailsService.addProfileDetailsToCollectionIfMissing<IProfileDetails>(
            profileDetails,
            ...(this.location?.profileDetails ?? []),
          ),
        ),
      )
      .subscribe((profileDetails: IProfileDetails[]) => (this.profileDetailsSharedCollection = profileDetails));

    this.userDetailsService
      .query()
      .pipe(map((res: HttpResponse<IUserDetails[]>) => res.body ?? []))
      .pipe(
        map((userDetails: IUserDetails[]) =>
          this.userDetailsService.addUserDetailsToCollectionIfMissing<IUserDetails>(userDetails, ...(this.location?.users ?? [])),
        ),
      )
      .subscribe((userDetails: IUserDetails[]) => (this.userDetailsSharedCollection = userDetails));
  }
}
