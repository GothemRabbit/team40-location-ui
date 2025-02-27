import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUserSearchHistory } from '../user-search-history.model';
import { UserSearchHistoryService } from '../service/user-search-history.service';
import { UserSearchHistoryFormGroup, UserSearchHistoryFormService } from './user-search-history-form.service';

@Component({
  standalone: true,
  selector: 'jhi-user-search-history-update',
  templateUrl: './user-search-history-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class UserSearchHistoryUpdateComponent implements OnInit {
  isSaving = false;
  userSearchHistory: IUserSearchHistory | null = null;

  protected userSearchHistoryService = inject(UserSearchHistoryService);
  protected userSearchHistoryFormService = inject(UserSearchHistoryFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: UserSearchHistoryFormGroup = this.userSearchHistoryFormService.createUserSearchHistoryFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userSearchHistory }) => {
      this.userSearchHistory = userSearchHistory;
      if (userSearchHistory) {
        this.updateForm(userSearchHistory);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const userSearchHistory = this.userSearchHistoryFormService.getUserSearchHistory(this.editForm);
    if (userSearchHistory.id !== null) {
      this.subscribeToSaveResponse(this.userSearchHistoryService.update(userSearchHistory));
    } else {
      this.subscribeToSaveResponse(this.userSearchHistoryService.create(userSearchHistory));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserSearchHistory>>): void {
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

  protected updateForm(userSearchHistory: IUserSearchHistory): void {
    this.userSearchHistory = userSearchHistory;
    this.userSearchHistoryFormService.resetForm(this.editForm, userSearchHistory);
  }
}
