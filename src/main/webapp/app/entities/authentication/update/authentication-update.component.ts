import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAuthentication } from '../authentication.model';
import { AuthenticationService } from '../service/authentication.service';
import { AuthenticationFormGroup, AuthenticationFormService } from './authentication-form.service';

@Component({
  standalone: true,
  selector: 'jhi-authentication-update',
  templateUrl: './authentication-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AuthenticationUpdateComponent implements OnInit {
  isSaving = false;
  authentication: IAuthentication | null = null;

  protected authenticationService = inject(AuthenticationService);
  protected authenticationFormService = inject(AuthenticationFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: AuthenticationFormGroup = this.authenticationFormService.createAuthenticationFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ authentication }) => {
      this.authentication = authentication;
      if (authentication) {
        this.updateForm(authentication);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const authentication = this.authenticationFormService.getAuthentication(this.editForm);
    if (authentication.id !== null) {
      this.subscribeToSaveResponse(this.authenticationService.update(authentication));
    } else {
      this.subscribeToSaveResponse(this.authenticationService.create(authentication));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAuthentication>>): void {
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

  protected updateForm(authentication: IAuthentication): void {
    this.authentication = authentication;
    this.authenticationFormService.resetForm(this.editForm, authentication);
  }
}
