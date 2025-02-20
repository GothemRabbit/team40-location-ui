import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { InteractionType } from 'app/entities/enumerations/interaction-type.model';
import { IUserInteraction } from '../user-interaction.model';
import { UserInteractionService } from '../service/user-interaction.service';
import { UserInteractionFormGroup, UserInteractionFormService } from './user-interaction-form.service';

@Component({
  standalone: true,
  selector: 'jhi-user-interaction-update',
  templateUrl: './user-interaction-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class UserInteractionUpdateComponent implements OnInit {
  isSaving = false;
  userInteraction: IUserInteraction | null = null;
  interactionTypeValues = Object.keys(InteractionType);

  protected userInteractionService = inject(UserInteractionService);
  protected userInteractionFormService = inject(UserInteractionFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: UserInteractionFormGroup = this.userInteractionFormService.createUserInteractionFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userInteraction }) => {
      this.userInteraction = userInteraction;
      if (userInteraction) {
        this.updateForm(userInteraction);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const userInteraction = this.userInteractionFormService.getUserInteraction(this.editForm);
    if (userInteraction.id !== null) {
      this.subscribeToSaveResponse(this.userInteractionService.update(userInteraction));
    } else {
      this.subscribeToSaveResponse(this.userInteractionService.create(userInteraction));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserInteraction>>): void {
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

  protected updateForm(userInteraction: IUserInteraction): void {
    this.userInteraction = userInteraction;
    this.userInteractionFormService.resetForm(this.editForm, userInteraction);
  }
}
