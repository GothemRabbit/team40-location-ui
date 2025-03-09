import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IItem } from 'app/entities/item/item.model';
import { ItemService } from 'app/entities/item/service/item.service';
import { IConversation } from 'app/entities/conversation/conversation.model';
import { ConversationService } from 'app/entities/conversation/service/conversation.service';
import { IProfileDetails } from 'app/entities/profile-details/profile-details.model';
import { ProfileDetailsService } from 'app/entities/profile-details/service/profile-details.service';
import { ILocation } from 'app/entities/location/location.model';
import { LocationService } from 'app/entities/location/service/location.service';
import { IProductStatus } from '../product-status.model';
import { ProductStatusService } from '../service/product-status.service';
import { ProductStatusFormService } from './product-status-form.service';

import { ProductStatusUpdateComponent } from './product-status-update.component';

describe('ProductStatus Management Update Component', () => {
  let comp: ProductStatusUpdateComponent;
  let fixture: ComponentFixture<ProductStatusUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let productStatusFormService: ProductStatusFormService;
  let productStatusService: ProductStatusService;
  let itemService: ItemService;
  let conversationService: ConversationService;
  let profileDetailsService: ProfileDetailsService;
  let locationService: LocationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ProductStatusUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ProductStatusUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProductStatusUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    productStatusFormService = TestBed.inject(ProductStatusFormService);
    productStatusService = TestBed.inject(ProductStatusService);
    itemService = TestBed.inject(ItemService);
    conversationService = TestBed.inject(ConversationService);
    profileDetailsService = TestBed.inject(ProfileDetailsService);
    locationService = TestBed.inject(LocationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call item query and add missing value', () => {
      const productStatus: IProductStatus = { id: 456 };
      const item: IItem = { id: 26941 };
      productStatus.item = item;

      const itemCollection: IItem[] = [{ id: 304 }];
      jest.spyOn(itemService, 'query').mockReturnValue(of(new HttpResponse({ body: itemCollection })));
      const expectedCollection: IItem[] = [item, ...itemCollection];
      jest.spyOn(itemService, 'addItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productStatus });
      comp.ngOnInit();

      expect(itemService.query).toHaveBeenCalled();
      expect(itemService.addItemToCollectionIfMissing).toHaveBeenCalledWith(itemCollection, item);
      expect(comp.itemsCollection).toEqual(expectedCollection);
    });

    it('Should call conversation query and add missing value', () => {
      const productStatus: IProductStatus = { id: 456 };
      const conversation: IConversation = { id: 23303 };
      productStatus.conversation = conversation;

      const conversationCollection: IConversation[] = [{ id: 16257 }];
      jest.spyOn(conversationService, 'query').mockReturnValue(of(new HttpResponse({ body: conversationCollection })));
      const expectedCollection: IConversation[] = [conversation, ...conversationCollection];
      jest.spyOn(conversationService, 'addConversationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productStatus });
      comp.ngOnInit();

      expect(conversationService.query).toHaveBeenCalled();
      expect(conversationService.addConversationToCollectionIfMissing).toHaveBeenCalledWith(conversationCollection, conversation);
      expect(comp.conversationsCollection).toEqual(expectedCollection);
    });

    it('Should call ProfileDetails query and add missing value', () => {
      const productStatus: IProductStatus = { id: 456 };
      const profileDetails: IProfileDetails = { id: 32171 };
      productStatus.profileDetails = profileDetails;

      const profileDetailsCollection: IProfileDetails[] = [{ id: 15834 }];
      jest.spyOn(profileDetailsService, 'query').mockReturnValue(of(new HttpResponse({ body: profileDetailsCollection })));
      const additionalProfileDetails = [profileDetails];
      const expectedCollection: IProfileDetails[] = [...additionalProfileDetails, ...profileDetailsCollection];
      jest.spyOn(profileDetailsService, 'addProfileDetailsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productStatus });
      comp.ngOnInit();

      expect(profileDetailsService.query).toHaveBeenCalled();
      expect(profileDetailsService.addProfileDetailsToCollectionIfMissing).toHaveBeenCalledWith(
        profileDetailsCollection,
        ...additionalProfileDetails.map(expect.objectContaining),
      );
      expect(comp.profileDetailsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Location query and add missing value', () => {
      const productStatus: IProductStatus = { id: 456 };
      const location: ILocation = { id: 25624 };
      productStatus.location = location;

      const locationCollection: ILocation[] = [{ id: 6740 }];
      jest.spyOn(locationService, 'query').mockReturnValue(of(new HttpResponse({ body: locationCollection })));
      const additionalLocations = [location];
      const expectedCollection: ILocation[] = [...additionalLocations, ...locationCollection];
      jest.spyOn(locationService, 'addLocationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productStatus });
      comp.ngOnInit();

      expect(locationService.query).toHaveBeenCalled();
      expect(locationService.addLocationToCollectionIfMissing).toHaveBeenCalledWith(
        locationCollection,
        ...additionalLocations.map(expect.objectContaining),
      );
      expect(comp.locationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const productStatus: IProductStatus = { id: 456 };
      const item: IItem = { id: 30264 };
      productStatus.item = item;
      const conversation: IConversation = { id: 21378 };
      productStatus.conversation = conversation;
      const profileDetails: IProfileDetails = { id: 616 };
      productStatus.profileDetails = profileDetails;
      const location: ILocation = { id: 5755 };
      productStatus.location = location;

      activatedRoute.data = of({ productStatus });
      comp.ngOnInit();

      expect(comp.itemsCollection).toContain(item);
      expect(comp.conversationsCollection).toContain(conversation);
      expect(comp.profileDetailsSharedCollection).toContain(profileDetails);
      expect(comp.locationsSharedCollection).toContain(location);
      expect(comp.productStatus).toEqual(productStatus);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProductStatus>>();
      const productStatus = { id: 123 };
      jest.spyOn(productStatusFormService, 'getProductStatus').mockReturnValue(productStatus);
      jest.spyOn(productStatusService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productStatus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: productStatus }));
      saveSubject.complete();

      // THEN
      expect(productStatusFormService.getProductStatus).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(productStatusService.update).toHaveBeenCalledWith(expect.objectContaining(productStatus));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProductStatus>>();
      const productStatus = { id: 123 };
      jest.spyOn(productStatusFormService, 'getProductStatus').mockReturnValue({ id: null });
      jest.spyOn(productStatusService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productStatus: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: productStatus }));
      saveSubject.complete();

      // THEN
      expect(productStatusFormService.getProductStatus).toHaveBeenCalled();
      expect(productStatusService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProductStatus>>();
      const productStatus = { id: 123 };
      jest.spyOn(productStatusService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ productStatus });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(productStatusService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareItem', () => {
      it('Should forward to itemService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(itemService, 'compareItem');
        comp.compareItem(entity, entity2);
        expect(itemService.compareItem).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareConversation', () => {
      it('Should forward to conversationService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(conversationService, 'compareConversation');
        comp.compareConversation(entity, entity2);
        expect(conversationService.compareConversation).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareProfileDetails', () => {
      it('Should forward to profileDetailsService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(profileDetailsService, 'compareProfileDetails');
        comp.compareProfileDetails(entity, entity2);
        expect(profileDetailsService.compareProfileDetails).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareLocation', () => {
      it('Should forward to locationService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(locationService, 'compareLocation');
        comp.compareLocation(entity, entity2);
        expect(locationService.compareLocation).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
