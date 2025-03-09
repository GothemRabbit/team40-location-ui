import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IWishlist } from 'app/entities/wishlist/wishlist.model';
import { WishlistService } from 'app/entities/wishlist/service/wishlist.service';
import { IProfileDetails } from 'app/entities/profile-details/profile-details.model';
import { ProfileDetailsService } from 'app/entities/profile-details/service/profile-details.service';
import { IUserDetails } from 'app/entities/user-details/user-details.model';
import { UserDetailsService } from 'app/entities/user-details/service/user-details.service';
import { IItem } from '../item.model';
import { ItemService } from '../service/item.service';
import { ItemFormService } from './item-form.service';

import { ItemUpdateComponent } from './item-update.component';

describe('Item Management Update Component', () => {
  let comp: ItemUpdateComponent;
  let fixture: ComponentFixture<ItemUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let itemFormService: ItemFormService;
  let itemService: ItemService;
  let wishlistService: WishlistService;
  let profileDetailsService: ProfileDetailsService;
  let userDetailsService: UserDetailsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ItemUpdateComponent],
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
      .overrideTemplate(ItemUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ItemUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    itemFormService = TestBed.inject(ItemFormService);
    itemService = TestBed.inject(ItemService);
    wishlistService = TestBed.inject(WishlistService);
    profileDetailsService = TestBed.inject(ProfileDetailsService);
    userDetailsService = TestBed.inject(UserDetailsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Wishlist query and add missing value', () => {
      const item: IItem = { id: 456 };
      const wishlists: IWishlist[] = [{ id: 4451 }];
      item.wishlists = wishlists;

      const wishlistCollection: IWishlist[] = [{ id: 12948 }];
      jest.spyOn(wishlistService, 'query').mockReturnValue(of(new HttpResponse({ body: wishlistCollection })));
      const additionalWishlists = [...wishlists];
      const expectedCollection: IWishlist[] = [...additionalWishlists, ...wishlistCollection];
      jest.spyOn(wishlistService, 'addWishlistToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ item });
      comp.ngOnInit();

      expect(wishlistService.query).toHaveBeenCalled();
      expect(wishlistService.addWishlistToCollectionIfMissing).toHaveBeenCalledWith(
        wishlistCollection,
        ...additionalWishlists.map(expect.objectContaining),
      );
      expect(comp.wishlistsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ProfileDetails query and add missing value', () => {
      const item: IItem = { id: 456 };
      const profileDetails: IProfileDetails = { id: 9361 };
      item.profileDetails = profileDetails;

      const profileDetailsCollection: IProfileDetails[] = [{ id: 585 }];
      jest.spyOn(profileDetailsService, 'query').mockReturnValue(of(new HttpResponse({ body: profileDetailsCollection })));
      const additionalProfileDetails = [profileDetails];
      const expectedCollection: IProfileDetails[] = [...additionalProfileDetails, ...profileDetailsCollection];
      jest.spyOn(profileDetailsService, 'addProfileDetailsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ item });
      comp.ngOnInit();

      expect(profileDetailsService.query).toHaveBeenCalled();
      expect(profileDetailsService.addProfileDetailsToCollectionIfMissing).toHaveBeenCalledWith(
        profileDetailsCollection,
        ...additionalProfileDetails.map(expect.objectContaining),
      );
      expect(comp.profileDetailsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call UserDetails query and add missing value', () => {
      const item: IItem = { id: 456 };
      const seller: IUserDetails = { id: 27779 };
      item.seller = seller;

      const userDetailsCollection: IUserDetails[] = [{ id: 17267 }];
      jest.spyOn(userDetailsService, 'query').mockReturnValue(of(new HttpResponse({ body: userDetailsCollection })));
      const additionalUserDetails = [seller];
      const expectedCollection: IUserDetails[] = [...additionalUserDetails, ...userDetailsCollection];
      jest.spyOn(userDetailsService, 'addUserDetailsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ item });
      comp.ngOnInit();

      expect(userDetailsService.query).toHaveBeenCalled();
      expect(userDetailsService.addUserDetailsToCollectionIfMissing).toHaveBeenCalledWith(
        userDetailsCollection,
        ...additionalUserDetails.map(expect.objectContaining),
      );
      expect(comp.userDetailsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const item: IItem = { id: 456 };
      const wishlist: IWishlist = { id: 6810 };
      item.wishlists = [wishlist];
      const profileDetails: IProfileDetails = { id: 30925 };
      item.profileDetails = profileDetails;
      const seller: IUserDetails = { id: 3123 };
      item.seller = seller;

      activatedRoute.data = of({ item });
      comp.ngOnInit();

      expect(comp.wishlistsSharedCollection).toContain(wishlist);
      expect(comp.profileDetailsSharedCollection).toContain(profileDetails);
      expect(comp.userDetailsSharedCollection).toContain(seller);
      expect(comp.item).toEqual(item);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IItem>>();
      const item = { id: 123 };
      jest.spyOn(itemFormService, 'getItem').mockReturnValue(item);
      jest.spyOn(itemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ item });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: item }));
      saveSubject.complete();

      // THEN
      expect(itemFormService.getItem).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(itemService.update).toHaveBeenCalledWith(expect.objectContaining(item));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IItem>>();
      const item = { id: 123 };
      jest.spyOn(itemFormService, 'getItem').mockReturnValue({ id: null });
      jest.spyOn(itemService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ item: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: item }));
      saveSubject.complete();

      // THEN
      expect(itemFormService.getItem).toHaveBeenCalled();
      expect(itemService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IItem>>();
      const item = { id: 123 };
      jest.spyOn(itemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ item });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(itemService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareWishlist', () => {
      it('Should forward to wishlistService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(wishlistService, 'compareWishlist');
        comp.compareWishlist(entity, entity2);
        expect(wishlistService.compareWishlist).toHaveBeenCalledWith(entity, entity2);
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

    describe('compareUserDetails', () => {
      it('Should forward to userDetailsService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userDetailsService, 'compareUserDetails');
        comp.compareUserDetails(entity, entity2);
        expect(userDetailsService.compareUserDetails).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
