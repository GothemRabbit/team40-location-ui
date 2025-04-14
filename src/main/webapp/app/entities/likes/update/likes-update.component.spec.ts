import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IItem } from 'app/entities/item/item.model';
import { ItemService } from 'app/entities/item/service/item.service';
import { IProfileDetails } from 'app/entities/profile-details/profile-details.model';
import { ProfileDetailsService } from 'app/entities/profile-details/service/profile-details.service';
import { ILikes } from '../likes.model';
import { LikesService } from '../service/likes.service';
import { LikesFormService } from './likes-form.service';

import { LikesUpdateComponent } from './likes-update.component';

describe('Likes Management Update Component', () => {
  let comp: LikesUpdateComponent;
  let fixture: ComponentFixture<LikesUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let likesFormService: LikesFormService;
  let likesService: LikesService;
  let itemService: ItemService;
  let profileDetailsService: ProfileDetailsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [LikesUpdateComponent],
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
      .overrideTemplate(LikesUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LikesUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    likesFormService = TestBed.inject(LikesFormService);
    likesService = TestBed.inject(LikesService);
    itemService = TestBed.inject(ItemService);
    profileDetailsService = TestBed.inject(ProfileDetailsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Item query and add missing value', () => {
      const likes: ILikes = { id: 456 };
      const item: IItem = { id: 31437 };
      likes.item = item;

      const itemCollection: IItem[] = [{ id: 11725 }];
      jest.spyOn(itemService, 'query').mockReturnValue(of(new HttpResponse({ body: itemCollection })));
      const additionalItems = [item];
      const expectedCollection: IItem[] = [...additionalItems, ...itemCollection];
      jest.spyOn(itemService, 'addItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ likes });
      comp.ngOnInit();

      expect(itemService.query).toHaveBeenCalled();
      expect(itemService.addItemToCollectionIfMissing).toHaveBeenCalledWith(
        itemCollection,
        ...additionalItems.map(expect.objectContaining),
      );
      expect(comp.itemsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ProfileDetails query and add missing value', () => {
      const likes: ILikes = { id: 456 };
      const profileDetails: IProfileDetails = { id: 5592 };
      likes.profileDetails = profileDetails;

      const profileDetailsCollection: IProfileDetails[] = [{ id: 14509 }];
      jest.spyOn(profileDetailsService, 'query').mockReturnValue(of(new HttpResponse({ body: profileDetailsCollection })));
      const additionalProfileDetails = [profileDetails];
      const expectedCollection: IProfileDetails[] = [...additionalProfileDetails, ...profileDetailsCollection];
      jest.spyOn(profileDetailsService, 'addProfileDetailsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ likes });
      comp.ngOnInit();

      expect(profileDetailsService.query).toHaveBeenCalled();
      expect(profileDetailsService.addProfileDetailsToCollectionIfMissing).toHaveBeenCalledWith(
        profileDetailsCollection,
        ...additionalProfileDetails.map(expect.objectContaining),
      );
      expect(comp.profileDetailsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const likes: ILikes = { id: 456 };
      const item: IItem = { id: 13248 };
      likes.item = item;
      const profileDetails: IProfileDetails = { id: 7047 };
      likes.profileDetails = profileDetails;

      activatedRoute.data = of({ likes });
      comp.ngOnInit();

      expect(comp.itemsSharedCollection).toContain(item);
      expect(comp.profileDetailsSharedCollection).toContain(profileDetails);
      expect(comp.likes).toEqual(likes);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILikes>>();
      const likes = { id: 123 };
      jest.spyOn(likesFormService, 'getLikes').mockReturnValue(likes);
      jest.spyOn(likesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ likes });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: likes }));
      saveSubject.complete();

      // THEN
      expect(likesFormService.getLikes).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(likesService.update).toHaveBeenCalledWith(expect.objectContaining(likes));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILikes>>();
      const likes = { id: 123 };
      jest.spyOn(likesFormService, 'getLikes').mockReturnValue({ id: null });
      jest.spyOn(likesService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ likes: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: likes }));
      saveSubject.complete();

      // THEN
      expect(likesFormService.getLikes).toHaveBeenCalled();
      expect(likesService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILikes>>();
      const likes = { id: 123 };
      jest.spyOn(likesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ likes });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(likesService.update).toHaveBeenCalled();
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

    describe('compareProfileDetails', () => {
      it('Should forward to profileDetailsService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(profileDetailsService, 'compareProfileDetails');
        comp.compareProfileDetails(entity, entity2);
        expect(profileDetailsService.compareProfileDetails).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
