import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IItem } from 'app/entities/item/item.model';
import { ItemService } from 'app/entities/item/service/item.service';
import { IConversation } from 'app/entities/conversation/conversation.model';
import { ConversationService } from 'app/entities/conversation/service/conversation.service';
import { IUserDetails } from 'app/entities/user-details/user-details.model';
import { UserDetailsService } from 'app/entities/user-details/service/user-details.service';
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
  let userDetailsService: UserDetailsService;
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
    userDetailsService = TestBed.inject(UserDetailsService);
    locationService = TestBed.inject(LocationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call item query and add missing value', () => {
      const productStatus: IProductStatus = { id: 456 };
      const item: IItem = { id: 13513 };
      productStatus.item = item;

      const itemCollection: IItem[] = [{ id: 1935 }];
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
      const conversation: IConversation = { id: 2459 };
      productStatus.conversation = conversation;

      const conversationCollection: IConversation[] = [{ id: 6469 }];
      jest.spyOn(conversationService, 'query').mockReturnValue(of(new HttpResponse({ body: conversationCollection })));
      const expectedCollection: IConversation[] = [conversation, ...conversationCollection];
      jest.spyOn(conversationService, 'addConversationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productStatus });
      comp.ngOnInit();

      expect(conversationService.query).toHaveBeenCalled();
      expect(conversationService.addConversationToCollectionIfMissing).toHaveBeenCalledWith(conversationCollection, conversation);
      expect(comp.conversationsCollection).toEqual(expectedCollection);
    });

    it('Should call UserDetails query and add missing value', () => {
      const productStatus: IProductStatus = { id: 456 };
      const buyer: IUserDetails = { id: 32584 };
      productStatus.buyer = buyer;
      const seller: IUserDetails = { id: 12294 };
      productStatus.seller = seller;

      const userDetailsCollection: IUserDetails[] = [{ id: 12342 }];
      jest.spyOn(userDetailsService, 'query').mockReturnValue(of(new HttpResponse({ body: userDetailsCollection })));
      const additionalUserDetails = [buyer, seller];
      const expectedCollection: IUserDetails[] = [...additionalUserDetails, ...userDetailsCollection];
      jest.spyOn(userDetailsService, 'addUserDetailsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ productStatus });
      comp.ngOnInit();

      expect(userDetailsService.query).toHaveBeenCalled();
      expect(userDetailsService.addUserDetailsToCollectionIfMissing).toHaveBeenCalledWith(
        userDetailsCollection,
        ...additionalUserDetails.map(expect.objectContaining),
      );
      expect(comp.userDetailsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Location query and add missing value', () => {
      const productStatus: IProductStatus = { id: 456 };
      const meetingLocation: ILocation = { id: 11518 };
      productStatus.meetingLocation = meetingLocation;

      const locationCollection: ILocation[] = [{ id: 11188 }];
      jest.spyOn(locationService, 'query').mockReturnValue(of(new HttpResponse({ body: locationCollection })));
      const additionalLocations = [meetingLocation];
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
      const item: IItem = { id: 32254 };
      productStatus.item = item;
      const conversation: IConversation = { id: 24869 };
      productStatus.conversation = conversation;
      const buyer: IUserDetails = { id: 19176 };
      productStatus.buyer = buyer;
      const seller: IUserDetails = { id: 28546 };
      productStatus.seller = seller;
      const meetingLocation: ILocation = { id: 28913 };
      productStatus.meetingLocation = meetingLocation;

      activatedRoute.data = of({ productStatus });
      comp.ngOnInit();

      expect(comp.itemsCollection).toContain(item);
      expect(comp.conversationsCollection).toContain(conversation);
      expect(comp.userDetailsSharedCollection).toContain(buyer);
      expect(comp.userDetailsSharedCollection).toContain(seller);
      expect(comp.locationsSharedCollection).toContain(meetingLocation);
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

    describe('compareUserDetails', () => {
      it('Should forward to userDetailsService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userDetailsService, 'compareUserDetails');
        comp.compareUserDetails(entity, entity2);
        expect(userDetailsService.compareUserDetails).toHaveBeenCalledWith(entity, entity2);
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
