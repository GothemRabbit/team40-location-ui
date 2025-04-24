import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { Observable, take } from 'rxjs';
import { finalize, map } from 'rxjs/operators';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ItemDeleteDialogComponent } from '../delete/item-delete-dialog.component';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { filter, tap } from 'rxjs/operators'; // Already imported in some places

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IWishlist } from 'app/entities/wishlist/wishlist.model';
import { WishlistService } from 'app/entities/wishlist/service/wishlist.service';
import { IProfileDetails } from 'app/entities/profile-details/profile-details.model';
import { ProfileDetailsService } from 'app/entities/profile-details/service/profile-details.service';
import { IUserDetails } from 'app/entities/user-details/user-details.model';
import { UserDetailsService } from 'app/entities/user-details/service/user-details.service';
import { Condition } from 'app/entities/enumerations/condition.model';
import { Category } from 'app/entities/enumerations/category.model';
import { ItemService } from '../service/item.service';
import { IItem } from '../item.model';
import { ItemFormGroup, ItemFormService } from './item-form.service';
import { ImagesService } from 'app/entities/images/service/images.service';
import { IImages } from 'app/entities/images/images.model';
import { LoginService } from 'app/login/login.service';

@Component({
  standalone: true,
  selector: 'jhi-item-update',
  templateUrl: './item-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule, RouterLink],
  // styleUrl: 'item-update.component.scss',
  styleUrls: ['./item-update.component.scss'],
})
export class ItemUpdateComponent implements OnInit {
  isSaving = false;
  item: IItem | null = null;
  conditionValues = Object.keys(Condition);
  categoryValues = Object.keys(Category);

  wishlistsSharedCollection: IWishlist[] = [];
  profileDetailsSharedCollection: IProfileDetails[] = [];
  userDetailsSharedCollection: IUserDetails[] = [];
  newImages: { file: File; preview: string }[] = [];
  existingImages: IImages[] = [];
  toBeDeleted: IImages[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected itemService = inject(ItemService);
  protected itemFormService = inject(ItemFormService);
  protected wishlistService = inject(WishlistService);
  protected profileDetailsService = inject(ProfileDetailsService);
  protected userDetailsService = inject(UserDetailsService);
  protected activatedRoute = inject(ActivatedRoute);
  protected imagesService = inject(ImagesService);
  protected loginService = inject(LoginService);
  protected modalService = inject(NgbModal);
  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ItemFormGroup = this.itemFormService.createItemFormGroup();

  compareWishlist = (o1: IWishlist | null, o2: IWishlist | null): boolean => this.wishlistService.compareWishlist(o1, o2);

  compareProfileDetails = (o1: IProfileDetails | null, o2: IProfileDetails | null): boolean =>
    this.profileDetailsService.compareProfileDetails(o1, o2);

  compareUserDetails = (o1: IUserDetails | null, o2: IUserDetails | null): boolean => this.userDetailsService.compareUserDetails(o1, o2);
  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ item }) => {
      this.item = item;
      if (item) {
        this.updateForm(item);
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

  onFilesSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (!input.files) {
      return;
    }
    // Calculate the total number of images already associated with this item
    const totalExisting = this.existingImages.length;
    const totalNew = this.newImages.length;
    const totalSoFar = totalExisting + totalNew;
    const allowedRemaining = 10 - totalSoFar;

    // If no more images are allowed, notify the user and exit.
    if (allowedRemaining <= 0) {
      this.eventManager.broadcast(
        new EventWithContent<AlertError>('teamproject24App.error', {
          message: 'You can only upload a maximum of 10 images per item.',
        }),
      );
      return;
    }
    // If the number of files selected exceeds what is allowed, notify the user.
    if (input.files.length > allowedRemaining) {
      this.eventManager.broadcast(
        new EventWithContent<AlertError>('teamproject24App.error', {
          message: `Only a total of 10 images are allowed per item. The first ${allowedRemaining} image(s) have been selected.`,
        }),
      );
    }

    // Convert the FileList to an array and process only up to the allowed number
    const files = Array.from(input.files).slice(0, allowedRemaining);
    for (const file of files) {
      const reader = new FileReader();
      reader.onload = e => {
        const preview = e.target?.result as string;
        this.newImages.push({ file, preview });
      };
      reader.readAsDataURL(file);
    }
  }

  removeImage(index: number): void {
    this.newImages.splice(index, 1);
  }

  removeExistingImage(index: number): void {
    const imgToRemove = this.existingImages[index];
    this.existingImages.splice(index, 1);
    // Defer actual delete
    this.toBeDeleted.push(imgToRemove);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('teamproject24App.error', { message: err.message })),
    });
  }

  delete(item: IItem): void {
    const modalRef = this.modalService.open(ItemDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.item = item;
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        tap(() => this.previousState()),
      )
      .subscribe();
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const item = this.itemFormService.getItem(this.editForm);

    // Use LoginService to retrieve the current user's profile details, but only take the first emission.
    this.loginService
      .getProfileDetails()
      .pipe(take(1))
      .subscribe((profileDetails: IProfileDetails | undefined) => {
        if (profileDetails) {
          item.profileDetails = profileDetails;
        }
        item.images = this.existingImages;
        if (item.id !== null) {
          this.subscribeToSaveResponse(this.itemService.update(item));
        } else {
          this.subscribeToSaveResponse(this.itemService.create(item));
        }
      });
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IItem>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: response => {
        if (response.body) {
          this.item = response.body;
          this.deleteDeferredImages();
          // After saving the item, if there are new images, upload them.
          if (this.newImages.length > 0) {
            this.uploadImages(response.body.id);
          }
        }
        this.onSaveSuccess();
      },
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

  protected updateForm(item: IItem): void {
    this.item = item;
    this.itemFormService.resetForm(this.editForm, item);

    this.wishlistsSharedCollection = this.wishlistService.addWishlistToCollectionIfMissing<IWishlist>(
      this.wishlistsSharedCollection,
      ...(item.wishlists ?? []),
    );
    this.profileDetailsSharedCollection = this.profileDetailsService.addProfileDetailsToCollectionIfMissing<IProfileDetails>(
      this.profileDetailsSharedCollection,
      item.profileDetails,
    );
    this.userDetailsSharedCollection = this.userDetailsService.addUserDetailsToCollectionIfMissing<IUserDetails>(
      this.userDetailsSharedCollection,
      item.seller,
    );
    // <<--- NEW: Copy the existing images from the item into existingImages array
    if (item.images) {
      this.existingImages = [...item.images];
    }
  }

  protected loadRelationshipsOptions(): void {
    this.wishlistService
      .query()
      .pipe(map((res: HttpResponse<IWishlist[]>) => res.body ?? []))
      .pipe(
        map((wishlists: IWishlist[]) =>
          this.wishlistService.addWishlistToCollectionIfMissing<IWishlist>(wishlists, ...(this.item?.wishlists ?? [])),
        ),
      )
      .subscribe((wishlists: IWishlist[]) => (this.wishlistsSharedCollection = wishlists));

    this.profileDetailsService
      .query()
      .pipe(map((res: HttpResponse<IProfileDetails[]>) => res.body ?? []))
      .pipe(
        map((profileDetails: IProfileDetails[]) =>
          this.profileDetailsService.addProfileDetailsToCollectionIfMissing<IProfileDetails>(profileDetails, this.item?.profileDetails),
        ),
      )
      .subscribe((profileDetails: IProfileDetails[]) => (this.profileDetailsSharedCollection = profileDetails));

    this.userDetailsService
      .query()
      .pipe(map((res: HttpResponse<IUserDetails[]>) => res.body ?? []))
      .pipe(
        map((userDetails: IUserDetails[]) =>
          this.userDetailsService.addUserDetailsToCollectionIfMissing<IUserDetails>(userDetails, this.item?.seller),
        ),
      )
      .subscribe((userDetails: IUserDetails[]) => (this.userDetailsSharedCollection = userDetails));
  }

  protected uploadImages(itemId: number): void {
    for (const imgObj of this.newImages) {
      const reader = new FileReader();
      reader.onload = e => {
        const result = e.target?.result as string;
        // Remove the data URL prefix if present.
        const base64Data = result.split(',')[1];
        const imagesDTO = {
          id: null,
          images: base64Data,
          imagesContentType: imgObj.file.type,
          item: { id: itemId },
        };
        this.imagesService.create(imagesDTO).subscribe({
          next() {
            // Optionally update UI or log success.
          },
          error(err) {
            console.error('Error uploading image:', err);
          },
        });
      };
      reader.readAsDataURL(imgObj.file);
    }
  }

  protected deleteDeferredImages(): void {
    for (const img of this.toBeDeleted) {
      if (img.id) {
        this.imagesService.delete(img.id).subscribe({
          next() {
            // success
          },
          error(err) {
            console.error('Error deleting deferred image:', err);
          },
        });
      }
    }
    // Clear out the array
    this.toBeDeleted = [];
  }

  // private processFiles(files: FileList): void {
  //   for (const file of Array.from(files)) {
  //     // Limit to 10 images if needed
  //     if (this.newImages.length >= 10) break;
  //     const reader = new FileReader();
  //     reader.onload = e => {
  //       const preview = e.target?.result as string;
  //       this.newImages.push({ file, preview });
  //     };
  //     reader.readAsDataURL(file);
  //   }
  // }
}
