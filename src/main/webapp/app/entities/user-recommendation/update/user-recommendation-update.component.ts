import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUserRecommendation } from '../user-recommendation.model';
import { UserRecommendationService } from '../service/user-recommendation.service';
import { UserRecommendationFormGroup, UserRecommendationFormService } from './user-recommendation-form.service';

@Component({
  standalone: true,
  selector: 'jhi-user-recommendation-update',
  templateUrl: './user-recommendation-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class UserRecommendationUpdateComponent implements OnInit {
  isSaving = false;
  userRecommendation: IUserRecommendation | null = null;

  protected userRecommendationService = inject(UserRecommendationService);
  protected userRecommendationFormService = inject(UserRecommendationFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: UserRecommendationFormGroup = this.userRecommendationFormService.createUserRecommendationFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userRecommendation }) => {
      this.userRecommendation = userRecommendation;
      if (userRecommendation) {
        this.updateForm(userRecommendation);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const userRecommendation = this.userRecommendationFormService.getUserRecommendation(this.editForm);
    if (userRecommendation.id !== null) {
      this.subscribeToSaveResponse(this.userRecommendationService.update(userRecommendation));
    } else {
      this.subscribeToSaveResponse(this.userRecommendationService.create(userRecommendation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserRecommendation>>): void {
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

  protected updateForm(userRecommendation: IUserRecommendation): void {
    this.userRecommendation = userRecommendation;
    this.userRecommendationFormService.resetForm(this.editForm, userRecommendation);
  }
}
