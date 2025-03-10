import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IProfileDetails } from 'app/entities/profile-details/profile-details.model';
import { ProfileDetailsService } from 'app/entities/profile-details/service/profile-details.service';
import { IItem } from 'app/entities/item/item.model';
import { ItemService } from 'app/entities/item/service/item.service';
import { IUserDetails } from 'app/entities/user-details/user-details.model';
import { UserDetailsService } from 'app/entities/user-details/service/user-details.service';
import { IWishlist } from '../wishlist.model';
import { WishlistService } from '../service/wishlist.service';
import { WishlistFormService } from './wishlist-form.service';

import { WishlistUpdateComponent } from './wishlist-update.component';

describe('Wishlist Management Update Component', () => {
  let comp: WishlistUpdateComponent;
  let fixture: ComponentFixture<WishlistUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let wishlistFormService: WishlistFormService;
  let wishlistService: WishlistService;
  let profileDetailsService: ProfileDetailsService;
  let itemService: ItemService;
  let userDetailsService: UserDetailsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [WishlistUpdateComponent],
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
      .overrideTemplate(WishlistUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(WishlistUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    wishlistFormService = TestBed.inject(WishlistFormService);
    wishlistService = TestBed.inject(WishlistService);
    profileDetailsService = TestBed.inject(ProfileDetailsService);
    itemService = TestBed.inject(ItemService);
    userDetailsService = TestBed.inject(UserDetailsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ProfileDetails query and add missing value', () => {
      const wishlist: IWishlist = { id: 456 };
      const profileDetails: IProfileDetails = { id: 14509 };
      wishlist.profileDetails = profileDetails;

      const profileDetailsCollection: IProfileDetails[] = [{ id: 7047 }];
      jest.spyOn(profileDetailsService, 'query').mockReturnValue(of(new HttpResponse({ body: profileDetailsCollection })));
      const additionalProfileDetails = [profileDetails];
      const expectedCollection: IProfileDetails[] = [...additionalProfileDetails, ...profileDetailsCollection];
      jest.spyOn(profileDetailsService, 'addProfileDetailsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ wishlist });
      comp.ngOnInit();

      expect(profileDetailsService.query).toHaveBeenCalled();
      expect(profileDetailsService.addProfileDetailsToCollectionIfMissing).toHaveBeenCalledWith(
        profileDetailsCollection,
        ...additionalProfileDetails.map(expect.objectContaining),
      );
      expect(comp.profileDetailsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Item query and add missing value', () => {
      const wishlist: IWishlist = { id: 456 };
      const items: IItem[] = [{ id: 3697 }];
      wishlist.items = items;

      const itemCollection: IItem[] = [{ id: 1676 }];
      jest.spyOn(itemService, 'query').mockReturnValue(of(new HttpResponse({ body: itemCollection })));
      const additionalItems = [...items];
      const expectedCollection: IItem[] = [...additionalItems, ...itemCollection];
      jest.spyOn(itemService, 'addItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ wishlist });
      comp.ngOnInit();

      expect(itemService.query).toHaveBeenCalled();
      expect(itemService.addItemToCollectionIfMissing).toHaveBeenCalledWith(
        itemCollection,
        ...additionalItems.map(expect.objectContaining),
      );
      expect(comp.itemsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call UserDetails query and add missing value', () => {
      const wishlist: IWishlist = { id: 456 };
      const userDetails: IUserDetails = { id: 148 };
      wishlist.userDetails = userDetails;

      const userDetailsCollection: IUserDetails[] = [{ id: 23780 }];
      jest.spyOn(userDetailsService, 'query').mockReturnValue(of(new HttpResponse({ body: userDetailsCollection })));
      const additionalUserDetails = [userDetails];
      const expectedCollection: IUserDetails[] = [...additionalUserDetails, ...userDetailsCollection];
      jest.spyOn(userDetailsService, 'addUserDetailsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ wishlist });
      comp.ngOnInit();

      expect(userDetailsService.query).toHaveBeenCalled();
      expect(userDetailsService.addUserDetailsToCollectionIfMissing).toHaveBeenCalledWith(
        userDetailsCollection,
        ...additionalUserDetails.map(expect.objectContaining),
      );
      expect(comp.userDetailsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const wishlist: IWishlist = { id: 456 };
      const profileDetails: IProfileDetails = { id: 1785 };
      wishlist.profileDetails = profileDetails;
      const item: IItem = { id: 5469 };
      wishlist.items = [item];
      const userDetails: IUserDetails = { id: 29510 };
      wishlist.userDetails = userDetails;

      activatedRoute.data = of({ wishlist });
      comp.ngOnInit();

      expect(comp.profileDetailsSharedCollection).toContain(profileDetails);
      expect(comp.itemsSharedCollection).toContain(item);
      expect(comp.userDetailsSharedCollection).toContain(userDetails);
      expect(comp.wishlist).toEqual(wishlist);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWishlist>>();
      const wishlist = { id: 123 };
      jest.spyOn(wishlistFormService, 'getWishlist').mockReturnValue(wishlist);
      jest.spyOn(wishlistService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ wishlist });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: wishlist }));
      saveSubject.complete();

      // THEN
      expect(wishlistFormService.getWishlist).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(wishlistService.update).toHaveBeenCalledWith(expect.objectContaining(wishlist));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWishlist>>();
      const wishlist = { id: 123 };
      jest.spyOn(wishlistFormService, 'getWishlist').mockReturnValue({ id: null });
      jest.spyOn(wishlistService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ wishlist: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: wishlist }));
      saveSubject.complete();

      // THEN
      expect(wishlistFormService.getWishlist).toHaveBeenCalled();
      expect(wishlistService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWishlist>>();
      const wishlist = { id: 123 };
      jest.spyOn(wishlistService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ wishlist });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(wishlistService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareProfileDetails', () => {
      it('Should forward to profileDetailsService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(profileDetailsService, 'compareProfileDetails');
        comp.compareProfileDetails(entity, entity2);
        expect(profileDetailsService.compareProfileDetails).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareItem', () => {
      it('Should forward to itemService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(itemService, 'compareItem');
        comp.compareItem(entity, entity2);
        expect(itemService.compareItem).toHaveBeenCalledWith(entity, entity2);
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
