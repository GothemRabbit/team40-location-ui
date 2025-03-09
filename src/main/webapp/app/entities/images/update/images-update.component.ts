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
import { IItem } from 'app/entities/item/item.model';
import { ItemService } from 'app/entities/item/service/item.service';
import { ImagesService } from '../service/images.service';
import { IImages } from '../images.model';
import { ImagesFormGroup, ImagesFormService } from './images-form.service';

@Component({
  standalone: true,
  selector: 'jhi-images-update',
  templateUrl: './images-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ImagesUpdateComponent implements OnInit {
  isSaving = false;
  images: IImages | null = null;

  itemsSharedCollection: IItem[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected imagesService = inject(ImagesService);
  protected imagesFormService = inject(ImagesFormService);
  protected itemService = inject(ItemService);
  protected elementRef = inject(ElementRef);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ImagesFormGroup = this.imagesFormService.createImagesFormGroup();

  compareItem = (o1: IItem | null, o2: IItem | null): boolean => this.itemService.compareItem(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ images }) => {
      this.images = images;
      if (images) {
        this.updateForm(images);
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
    const images = this.imagesFormService.getImages(this.editForm);
    if (images.id !== null) {
      this.subscribeToSaveResponse(this.imagesService.update(images));
    } else {
      this.subscribeToSaveResponse(this.imagesService.create(images));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IImages>>): void {
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

  protected updateForm(images: IImages): void {
    this.images = images;
    this.imagesFormService.resetForm(this.editForm, images);

    this.itemsSharedCollection = this.itemService.addItemToCollectionIfMissing<IItem>(this.itemsSharedCollection, images.item);
  }

  protected loadRelationshipsOptions(): void {
    this.itemService
      .query()
      .pipe(map((res: HttpResponse<IItem[]>) => res.body ?? []))
      .pipe(map((items: IItem[]) => this.itemService.addItemToCollectionIfMissing<IItem>(items, this.images?.item)))
      .subscribe((items: IItem[]) => (this.itemsSharedCollection = items));
  }
}
