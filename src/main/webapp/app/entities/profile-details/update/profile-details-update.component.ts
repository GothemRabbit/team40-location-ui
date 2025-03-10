import { Component, ElementRef, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { ILocation } from 'app/entities/location/location.model';
import { LocationService } from 'app/entities/location/service/location.service';
import { IConversation } from 'app/entities/conversation/conversation.model';
import { ConversationService } from 'app/entities/conversation/service/conversation.service';
import { ProfileDetailsService } from '../service/profile-details.service';
import { IProfileDetails } from '../profile-details.model';
import { ProfileDetailsFormGroup, ProfileDetailsFormService } from './profile-details-form.service';

@Component({
  standalone: true,
  selector: 'jhi-profile-details-update',
  templateUrl: './profile-details-update.component.html',
  styleUrl: './profile-details-update.component.scss',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ProfileDetailsUpdateComponent implements OnInit {
  activeTab = 'profileDetails';
  isSaving = false;
  profileDetails: IProfileDetails | null = null;

  usersSharedCollection: IUser[] = [];
  locationsSharedCollection: ILocation[] = [];
  conversationsSharedCollection: IConversation[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected profileDetailsService = inject(ProfileDetailsService);
  protected profileDetailsFormService = inject(ProfileDetailsFormService);
  protected userService = inject(UserService);
  protected locationService = inject(LocationService);
  protected conversationService = inject(ConversationService);
  protected elementRef = inject(ElementRef);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProfileDetailsFormGroup = this.profileDetailsFormService.createProfileDetailsFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareLocation = (o1: ILocation | null, o2: ILocation | null): boolean => this.locationService.compareLocation(o1, o2);

  compareConversation = (o1: IConversation | null, o2: IConversation | null): boolean =>
    this.conversationService.compareConversation(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ profileDetails }) => {
      this.profileDetails = profileDetails;
      if (profileDetails) {
        this.updateForm(profileDetails);
      }

      this.loadRelationshipsOptions();
    });
  }

  setActiveTab(tabName: string): void {
    this.activeTab = tabName;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('teamproject24App.error', { message: err.message })),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector(`#${idInput}`)) {
      this.elementRef.nativeElement.querySelector(`#${idInput}`).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const profileDetails = this.profileDetailsFormService.getProfileDetails(this.editForm);
    if (profileDetails.id !== null) {
      this.subscribeToSaveResponse(this.profileDetailsService.update(profileDetails));
    } else {
      this.subscribeToSaveResponse(this.profileDetailsService.create(profileDetails));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProfileDetails>>): void {
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

  protected updateForm(profileDetails: IProfileDetails): void {
    this.profileDetails = profileDetails;
    this.profileDetailsFormService.resetForm(this.editForm, profileDetails);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, profileDetails.user);
    this.locationsSharedCollection = this.locationService.addLocationToCollectionIfMissing<ILocation>(
      this.locationsSharedCollection,
      ...(profileDetails.locations ?? []),
    );
    this.conversationsSharedCollection = this.conversationService.addConversationToCollectionIfMissing<IConversation>(
      this.conversationsSharedCollection,
      ...(profileDetails.conversations ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.profileDetails?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.locationService
      .query()
      .pipe(map((res: HttpResponse<ILocation[]>) => res.body ?? []))
      .pipe(
        map((locations: ILocation[]) =>
          this.locationService.addLocationToCollectionIfMissing<ILocation>(locations, ...(this.profileDetails?.locations ?? [])),
        ),
      )
      .subscribe((locations: ILocation[]) => (this.locationsSharedCollection = locations));

    this.conversationService
      .query()
      .pipe(map((res: HttpResponse<IConversation[]>) => res.body ?? []))
      .pipe(
        map((conversations: IConversation[]) =>
          this.conversationService.addConversationToCollectionIfMissing<IConversation>(
            conversations,
            ...(this.profileDetails?.conversations ?? []),
          ),
        ),
      )
      .subscribe((conversations: IConversation[]) => (this.conversationsSharedCollection = conversations));
  }
}
