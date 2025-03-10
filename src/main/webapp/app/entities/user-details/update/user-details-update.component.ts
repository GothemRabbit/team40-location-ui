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
import { UserDetailsService } from '../service/user-details.service';
import { IUserDetails } from '../user-details.model';
import { UserDetailsFormGroup, UserDetailsFormService } from './user-details-form.service';

@Component({
  standalone: true,
  selector: 'jhi-user-details-update',
  templateUrl: './user-details-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class UserDetailsUpdateComponent implements OnInit {
  isSaving = false;
  userDetails: IUserDetails | null = null;

  usersSharedCollection: IUser[] = [];
  locationsSharedCollection: ILocation[] = [];
  conversationsSharedCollection: IConversation[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected userDetailsService = inject(UserDetailsService);
  protected userDetailsFormService = inject(UserDetailsFormService);
  protected userService = inject(UserService);
  protected locationService = inject(LocationService);
  protected conversationService = inject(ConversationService);
  protected elementRef = inject(ElementRef);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: UserDetailsFormGroup = this.userDetailsFormService.createUserDetailsFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareLocation = (o1: ILocation | null, o2: ILocation | null): boolean => this.locationService.compareLocation(o1, o2);

  compareConversation = (o1: IConversation | null, o2: IConversation | null): boolean =>
    this.conversationService.compareConversation(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userDetails }) => {
      this.userDetails = userDetails;
      if (userDetails) {
        this.updateForm(userDetails);
      }

      this.loadRelationshipsOptions();
    });
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
    const userDetails = this.userDetailsFormService.getUserDetails(this.editForm);
    if (userDetails.id !== null) {
      this.subscribeToSaveResponse(this.userDetailsService.update(userDetails));
    } else {
      this.subscribeToSaveResponse(this.userDetailsService.create(userDetails));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserDetails>>): void {
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

  protected updateForm(userDetails: IUserDetails): void {
    this.userDetails = userDetails;
    this.userDetailsFormService.resetForm(this.editForm, userDetails);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, userDetails.user);
    this.locationsSharedCollection = this.locationService.addLocationToCollectionIfMissing<ILocation>(
      this.locationsSharedCollection,
      ...(userDetails.meetupLocations ?? []),
    );
    this.conversationsSharedCollection = this.conversationService.addConversationToCollectionIfMissing<IConversation>(
      this.conversationsSharedCollection,
      ...(userDetails.chats ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.userDetails?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.locationService
      .query()
      .pipe(map((res: HttpResponse<ILocation[]>) => res.body ?? []))
      .pipe(
        map((locations: ILocation[]) =>
          this.locationService.addLocationToCollectionIfMissing<ILocation>(locations, ...(this.userDetails?.meetupLocations ?? [])),
        ),
      )
      .subscribe((locations: ILocation[]) => (this.locationsSharedCollection = locations));

    this.conversationService
      .query()
      .pipe(map((res: HttpResponse<IConversation[]>) => res.body ?? []))
      .pipe(
        map((conversations: IConversation[]) =>
          this.conversationService.addConversationToCollectionIfMissing<IConversation>(conversations, ...(this.userDetails?.chats ?? [])),
        ),
      )
      .subscribe((conversations: IConversation[]) => (this.conversationsSharedCollection = conversations));
  }
}
