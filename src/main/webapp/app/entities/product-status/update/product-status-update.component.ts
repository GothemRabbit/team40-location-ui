import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IItem } from 'app/entities/item/item.model';
import { ItemService } from 'app/entities/item/service/item.service';
import { IConversation } from 'app/entities/conversation/conversation.model';
import { ConversationService } from 'app/entities/conversation/service/conversation.service';
import { IProfileDetails } from 'app/entities/profile-details/profile-details.model';
import { ProfileDetailsService } from 'app/entities/profile-details/service/profile-details.service';
import { ILocation } from 'app/entities/location/location.model';
import { LocationService } from 'app/entities/location/service/location.service';
import { ProductState } from 'app/entities/enumerations/product-state.model';
import { ProductStatusService } from '../service/product-status.service';
import { IProductStatus } from '../product-status.model';
import { ProductStatusFormGroup, ProductStatusFormService } from './product-status-form.service';

@Component({
  standalone: true,
  selector: 'jhi-product-status-update',
  templateUrl: './product-status-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ProductStatusUpdateComponent implements OnInit {
  isSaving = false;
  productStatus: IProductStatus | null = null;
  productStateValues = Object.keys(ProductState);

  itemsCollection: IItem[] = [];
  conversationsCollection: IConversation[] = [];
  profileDetailsSharedCollection: IProfileDetails[] = [];
  locationsSharedCollection: ILocation[] = [];

  protected productStatusService = inject(ProductStatusService);
  protected productStatusFormService = inject(ProductStatusFormService);
  protected itemService = inject(ItemService);
  protected conversationService = inject(ConversationService);
  protected profileDetailsService = inject(ProfileDetailsService);
  protected locationService = inject(LocationService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ProductStatusFormGroup = this.productStatusFormService.createProductStatusFormGroup();

  compareItem = (o1: IItem | null, o2: IItem | null): boolean => this.itemService.compareItem(o1, o2);

  compareConversation = (o1: IConversation | null, o2: IConversation | null): boolean =>
    this.conversationService.compareConversation(o1, o2);

  compareProfileDetails = (o1: IProfileDetails | null, o2: IProfileDetails | null): boolean =>
    this.profileDetailsService.compareProfileDetails(o1, o2);

  compareLocation = (o1: ILocation | null, o2: ILocation | null): boolean => this.locationService.compareLocation(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productStatus }) => {
      this.productStatus = productStatus;
      if (productStatus) {
        this.updateForm(productStatus);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const productStatus = this.productStatusFormService.getProductStatus(this.editForm);
    if (productStatus.id !== null) {
      this.subscribeToSaveResponse(this.productStatusService.update(productStatus));
    } else {
      this.subscribeToSaveResponse(this.productStatusService.create(productStatus));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProductStatus>>): void {
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

  protected updateForm(productStatus: IProductStatus): void {
    this.productStatus = productStatus;
    this.productStatusFormService.resetForm(this.editForm, productStatus);
    this.editForm.patchValue({
      profileDetails: productStatus.profileDetails,
      profileDetails1: productStatus.profileDetails1,
    });
    this.itemsCollection = this.itemService.addItemToCollectionIfMissing<IItem>(this.itemsCollection, productStatus.item);
    this.conversationsCollection = this.conversationService.addConversationToCollectionIfMissing<IConversation>(
      this.conversationsCollection,
      productStatus.conversation,
    );
    this.profileDetailsSharedCollection = this.profileDetailsService.addProfileDetailsToCollectionIfMissing<IProfileDetails>(
      this.profileDetailsSharedCollection,
      productStatus.profileDetails,
    );
    this.locationsSharedCollection = this.locationService.addLocationToCollectionIfMissing<ILocation>(
      this.locationsSharedCollection,
      productStatus.location,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.itemService
      .query({ 'productStatusId.specified': 'false' })
      .pipe(map((res: HttpResponse<IItem[]>) => res.body ?? []))
      .pipe(map((items: IItem[]) => this.itemService.addItemToCollectionIfMissing<IItem>(items, this.productStatus?.item)))
      .subscribe((items: IItem[]) => (this.itemsCollection = items));

    this.conversationService
      .query({ filter: 'productstatus-is-null' })
      .pipe(map((res: HttpResponse<IConversation[]>) => res.body ?? []))
      .pipe(
        map((conversations: IConversation[]) =>
          this.conversationService.addConversationToCollectionIfMissing<IConversation>(conversations, this.productStatus?.conversation),
        ),
      )
      .subscribe((conversations: IConversation[]) => (this.conversationsCollection = conversations));

    this.profileDetailsService
      .query()
      .pipe(map((res: HttpResponse<IProfileDetails[]>) => res.body ?? []))
      .pipe(
        map((profileDetails: IProfileDetails[]) =>
          this.profileDetailsService.addProfileDetailsToCollectionIfMissing<IProfileDetails>(
            profileDetails,
            this.productStatus?.profileDetails,
          ),
        ),
      )
      .subscribe((profileDetails: IProfileDetails[]) => (this.profileDetailsSharedCollection = profileDetails));

    this.locationService
      .query()
      .pipe(map((res: HttpResponse<ILocation[]>) => res.body ?? []))
      .pipe(
        map((locations: ILocation[]) =>
          this.locationService.addLocationToCollectionIfMissing<ILocation>(locations, this.productStatus?.location),
        ),
      )
      .subscribe((locations: ILocation[]) => (this.locationsSharedCollection = locations));
  }
}
