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
import { ConversationService } from '../service/conversation.service';
import { IConversation } from '../conversation.model';
import { ConversationFormGroup, ConversationFormService } from './conversation-form.service';

@Component({
  standalone: true,
  selector: 'jhi-conversation-update',
  templateUrl: './conversation-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ConversationUpdateComponent implements OnInit {
  isSaving = false;
  conversation: IConversation | null = null;

  profileDetailsSharedCollection: IProfileDetails[] = [];
  userDetailsSharedCollection: IUserDetails[] = [];

  protected conversationService = inject(ConversationService);
  protected conversationFormService = inject(ConversationFormService);
  protected profileDetailsService = inject(ProfileDetailsService);
  protected userDetailsService = inject(UserDetailsService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ConversationFormGroup = this.conversationFormService.createConversationFormGroup();

  compareProfileDetails = (o1: IProfileDetails | null, o2: IProfileDetails | null): boolean =>
    this.profileDetailsService.compareProfileDetails(o1, o2);

  compareUserDetails = (o1: IUserDetails | null, o2: IUserDetails | null): boolean => this.userDetailsService.compareUserDetails(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ conversation }) => {
      this.conversation = conversation;
      if (conversation) {
        this.updateForm(conversation);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const conversation = this.conversationFormService.getConversation(this.editForm);
    if (conversation.id !== null) {
      this.subscribeToSaveResponse(this.conversationService.update(conversation));
    } else {
      this.subscribeToSaveResponse(this.conversationService.create(conversation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IConversation>>): void {
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

  protected updateForm(conversation: IConversation): void {
    this.conversation = conversation;
    this.conversationFormService.resetForm(this.editForm, conversation);

    this.profileDetailsSharedCollection = this.profileDetailsService.addProfileDetailsToCollectionIfMissing<IProfileDetails>(
      this.profileDetailsSharedCollection,
      ...(conversation.profileDetails ?? []),
    );
    this.userDetailsSharedCollection = this.userDetailsService.addUserDetailsToCollectionIfMissing<IUserDetails>(
      this.userDetailsSharedCollection,
      ...(conversation.participants ?? []),
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
            ...(this.conversation?.profileDetails ?? []),
          ),
        ),
      )
      .subscribe((profileDetails: IProfileDetails[]) => (this.profileDetailsSharedCollection = profileDetails));

    this.userDetailsService
      .query()
      .pipe(map((res: HttpResponse<IUserDetails[]>) => res.body ?? []))
      .pipe(
        map((userDetails: IUserDetails[]) =>
          this.userDetailsService.addUserDetailsToCollectionIfMissing<IUserDetails>(
            userDetails,
            ...(this.conversation?.participants ?? []),
          ),
        ),
      )
      .subscribe((userDetails: IUserDetails[]) => (this.userDetailsSharedCollection = userDetails));
  }
}
