import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IItem } from 'app/entities/item/item.model';
import { ItemService } from 'app/entities/item/service/item.service';
import { IUserDetails } from 'app/entities/user-details/user-details.model';
import { UserDetailsService } from 'app/entities/user-details/service/user-details.service';
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
  let userDetailsService: UserDetailsService;

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
    userDetailsService = TestBed.inject(UserDetailsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Item query and add missing value', () => {
      const likes: ILikes = { id: 456 };
      const item: IItem = { id: 13085 };
      likes.item = item;

      const itemCollection: IItem[] = [{ id: 17994 }];
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

    it('Should call UserDetails query and add missing value', () => {
      const likes: ILikes = { id: 456 };
      const user: IUserDetails = { id: 22005 };
      likes.user = user;

      const userDetailsCollection: IUserDetails[] = [{ id: 21584 }];
      jest.spyOn(userDetailsService, 'query').mockReturnValue(of(new HttpResponse({ body: userDetailsCollection })));
      const additionalUserDetails = [user];
      const expectedCollection: IUserDetails[] = [...additionalUserDetails, ...userDetailsCollection];
      jest.spyOn(userDetailsService, 'addUserDetailsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ likes });
      comp.ngOnInit();

      expect(userDetailsService.query).toHaveBeenCalled();
      expect(userDetailsService.addUserDetailsToCollectionIfMissing).toHaveBeenCalledWith(
        userDetailsCollection,
        ...additionalUserDetails.map(expect.objectContaining),
      );
      expect(comp.userDetailsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const likes: ILikes = { id: 456 };
      const item: IItem = { id: 29824 };
      likes.item = item;
      const user: IUserDetails = { id: 16437 };
      likes.user = user;

      activatedRoute.data = of({ likes });
      comp.ngOnInit();

      expect(comp.itemsSharedCollection).toContain(item);
      expect(comp.userDetailsSharedCollection).toContain(user);
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
