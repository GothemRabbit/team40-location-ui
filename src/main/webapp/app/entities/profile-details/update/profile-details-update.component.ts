import { Component, ElementRef, OnInit, inject, signal } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { filter, Observable, tap } from 'rxjs';
import { finalize, map } from 'rxjs/operators';
import { Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';

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
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { ProfileDetailsDeleteDialogComponent } from '../delete/profile-details-delete-dialog.component';
import { ITEM_DELETED_EVENT } from '../../../config/navigation.constants';
import { PasswordService } from '../../../account/password/password.service';
import PasswordStrengthBarComponent from '../../../account/password/password-strength-bar/password-strength-bar.component';
import { LoginService } from '../../../login/login.service';

@Component({
  standalone: true,
  selector: 'jhi-profile-details-update',
  templateUrl: './profile-details-update.component.html',
  styleUrl: './profile-details-update.component.scss',
  imports: [SharedModule, FormsModule, ReactiveFormsModule, FaIconComponent, PasswordStrengthBarComponent],
})
export class ProfileDetailsUpdateComponent implements OnInit {
  activeTab = 'profileDetails';
  isSaving = false;
  profileDetails: IProfileDetails | null = null;
  userEmail: string | undefined = undefined;
  account: Account | null = null;
  textSize = 16;

  fontFamily = '';
  fontFamilyLabel = 'Default';
  fontFamilyOptions = [
    { label: 'Open Dyslexic', value: 'OpenDyslexic, sans-serif' },
    { label: 'Prompt', value: '' },
    // { label: 'Arial', value: 'Arial, sans-serif' },
    // { label: 'Georgia', value: 'Georgia, serif' },
    // { label: 'Sans-serif', value: 'system-ui, sans-serif' },
  ];

  doNotMatch = signal(false);
  error = signal(false);
  success = signal(false);
  passwordForm = new FormGroup({
    currentPassword: new FormControl('', { nonNullable: true, validators: Validators.required }),
    newPassword: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(4), Validators.maxLength(50)],
    }),
    confirmPassword: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(4), Validators.maxLength(50)],
    }),
  });

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
  protected accountService = inject(AccountService);
  protected router = inject(Router);
  protected modalService = inject(NgbModal);
  private readonly passwordService = inject(PasswordService);
  private readonly loginService = inject(LoginService);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProfileDetailsFormGroup = this.profileDetailsFormService.createProfileDetailsFormGroup();

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareLocation = (o1: ILocation | null, o2: ILocation | null): boolean => this.locationService.compareLocation(o1, o2);

  compareConversation = (o1: IConversation | null, o2: IConversation | null): boolean =>
    this.conversationService.compareConversation(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.queryParams.subscribe(params => {
      if (params['tab']) {
        this.activeTab = params['tab'];
      }
    });
    this.accountService.identity().subscribe(account => {
      this.account = account;
      this.userEmail = account?.email;
    });
    this.activatedRoute.data.subscribe(({ profileDetails }) => {
      this.profileDetails = profileDetails;
      if (profileDetails) {
        this.updateForm(profileDetails);
      }

      this.loadRelationshipsOptions();
    });

    const savedSize = localStorage.getItem('fontSize');
    if (savedSize) {
      this.textSize = +savedSize;
      this.applyFontSize(this.textSize);
    }

    const savedFamily = localStorage.getItem('fontFamily');
    if (savedFamily) {
      this.fontFamily = savedFamily;
      this.fontFamilyLabel = this.fontFamilyOptions.find(o => o.value === savedFamily)?.label ?? 'Default';
      this.applyFontFamily(savedFamily);
    } else {
      const dys = this.fontFamilyOptions.find(o => o.label === 'Open Dyslexic')!.value;
      this.fontFamily = dys;
      this.fontFamilyLabel = 'Open Dyslexic';
      this.applyFontFamily(dys);
      localStorage.setItem('fontFamily', dys);
    }
    // if (savedFamily !== null) {
    //   this.fontFamily = savedFamily;
    //   const opt = this.fontFamilyOptions.find(o => o.value === savedFamily);
    //   this.fontFamilyLabel = opt?.label ?? 'Default';
    //   this.applyFontFamily(savedFamily);
    // }
  }

  setActiveTab(tab: string): void {
    this.activeTab = tab;
    this.router.navigate([], {
      relativeTo: this.activatedRoute,
      queryParams: { tab },
      queryParamsHandling: 'merge',
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

  changePassword(): void {
    this.error.set(false);
    this.success.set(false);
    this.doNotMatch.set(false);

    const { newPassword, confirmPassword, currentPassword } = this.passwordForm.getRawValue();
    if (newPassword !== confirmPassword) {
      this.doNotMatch.set(true);
    } else {
      this.passwordService.save(newPassword, currentPassword).subscribe({
        next: () => this.success.set(true),
        error: () => this.error.set(true),
      });
    }
  }

  delete(profileDetails: IProfileDetails): void {
    const modalRef = this.modalService.open(ProfileDetailsDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.profileDetails = profileDetails;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        tap(() => {
          // Redirect to logout or homepage
          this.loginService.logout();
          this.router.navigate(['']);
        }),
      )
      .subscribe();
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

  toggleDarkMode(isDark: boolean): void {
    if (isDark) {
      document.body.classList.add('dark-mode');
    } else {
      document.body.classList.remove('dark-mode');
    }
    localStorage.setItem('darkMode', String(isDark));
  }

  public onSliderChange(event: Event): void {
    const value = +(event.target as HTMLInputElement).value;
    this.textSize = value;
    localStorage.setItem('fontSize', value.toString());
    this.applyFontSize(value);
  }

  onFontFamilyChange(event: Event): void {
    // eslint-disable-next-line no-console
    console.log('Font family picked:', (event.target as HTMLSelectElement).value);
    const sel = event.target as HTMLSelectElement;
    const val = sel.value;
    this.fontFamily = val;
    this.fontFamilyLabel = this.fontFamilyOptions.find(o => o.value === val)?.label ?? 'Default';
    localStorage.setItem('fontFamily', val);
    this.applyFontFamily(val);
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

  private applyFontSize(sizePx: number): void {
    document.documentElement.style.setProperty('--font-size', `${sizePx}px`);
  }
  private applyFontFamily(family: string): void {
    //   const cssVal = family || 'system-ui, sans-serif';
    //   document.documentElement.style.setProperty('--font-family', cssVal);
    // }
    if (!family) {
      document.documentElement.style.removeProperty('--font-family');
    } else {
      document.documentElement.style.setProperty('--font-family', family);
    }
  }
}
