import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IItem } from 'app/entities/item/item.model';
import { ItemService } from 'app/entities/item/service/item.service';
import { IProfileDetails } from 'app/entities/profile-details/profile-details.model';
import { ProfileDetailsService } from 'app/entities/profile-details/service/profile-details.service';
import { LikesService } from '../service/likes.service';
import { ILikes } from '../likes.model';
import { LikesFormGroup, LikesFormService } from './likes-form.service';

@Component({
  standalone: true,
  selector: 'jhi-likes-update',
  templateUrl: './likes-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class LikesUpdateComponent implements OnInit {
  isSaving = false;
  likes: ILikes | null = null;

  itemsSharedCollection: IItem[] = [];
  profileDetailsSharedCollection: IProfileDetails[] = [];

  protected likesService = inject(LikesService);
  protected likesFormService = inject(LikesFormService);
  protected itemService = inject(ItemService);
  protected profileDetailsService = inject(ProfileDetailsService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: LikesFormGroup = this.likesFormService.createLikesFormGroup();

  compareItem = (o1: IItem | null, o2: IItem | null): boolean => this.itemService.compareItem(o1, o2);

  compareProfileDetails = (o1: IProfileDetails | null, o2: IProfileDetails | null): boolean =>
    this.profileDetailsService.compareProfileDetails(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ likes }) => {
      this.likes = likes;
      if (likes) {
        this.updateForm(likes);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const likes = this.likesFormService.getLikes(this.editForm);
    if (likes.id !== null) {
      this.subscribeToSaveResponse(this.likesService.update(likes));
    } else {
      this.subscribeToSaveResponse(this.likesService.create(likes));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILikes>>): void {
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

  protected updateForm(likes: ILikes): void {
    this.likes = likes;
    this.likesFormService.resetForm(this.editForm, likes);

    this.itemsSharedCollection = this.itemService.addItemToCollectionIfMissing<IItem>(this.itemsSharedCollection, likes.item);
    this.profileDetailsSharedCollection = this.profileDetailsService.addProfileDetailsToCollectionIfMissing<IProfileDetails>(
      this.profileDetailsSharedCollection,
      likes.profileDetails,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.itemService
      .query()
      .pipe(map((res: HttpResponse<IItem[]>) => res.body ?? []))
      .pipe(map((items: IItem[]) => this.itemService.addItemToCollectionIfMissing<IItem>(items, this.likes?.item)))
      .subscribe((items: IItem[]) => (this.itemsSharedCollection = items));

    this.profileDetailsService
      .query()
      .pipe(map((res: HttpResponse<IProfileDetails[]>) => res.body ?? []))
      .pipe(
        map((profileDetails: IProfileDetails[]) =>
          this.profileDetailsService.addProfileDetailsToCollectionIfMissing<IProfileDetails>(profileDetails, this.likes?.profileDetails),
        ),
      )
      .subscribe((profileDetails: IProfileDetails[]) => (this.profileDetailsSharedCollection = profileDetails));
  }
}
