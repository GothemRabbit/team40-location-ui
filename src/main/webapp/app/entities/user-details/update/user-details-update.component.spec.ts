import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { ILocation } from 'app/entities/location/location.model';
import { LocationService } from 'app/entities/location/service/location.service';
import { IConversation } from 'app/entities/conversation/conversation.model';
import { ConversationService } from 'app/entities/conversation/service/conversation.service';
import { IUserDetails } from '../user-details.model';
import { UserDetailsService } from '../service/user-details.service';
import { UserDetailsFormService } from './user-details-form.service';

import { UserDetailsUpdateComponent } from './user-details-update.component';

describe('UserDetails Management Update Component', () => {
  let comp: UserDetailsUpdateComponent;
  let fixture: ComponentFixture<UserDetailsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let userDetailsFormService: UserDetailsFormService;
  let userDetailsService: UserDetailsService;
  let userService: UserService;
  let locationService: LocationService;
  let conversationService: ConversationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [UserDetailsUpdateComponent],
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
      .overrideTemplate(UserDetailsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UserDetailsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    userDetailsFormService = TestBed.inject(UserDetailsFormService);
    userDetailsService = TestBed.inject(UserDetailsService);
    userService = TestBed.inject(UserService);
    locationService = TestBed.inject(LocationService);
    conversationService = TestBed.inject(ConversationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const userDetails: IUserDetails = { id: 456 };
      const user: IUser = { id: 1503 };
      userDetails.user = user;

      const userCollection: IUser[] = [{ id: 7029 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ userDetails });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Location query and add missing value', () => {
      const userDetails: IUserDetails = { id: 456 };
      const meetupLocations: ILocation[] = [{ id: 25232 }];
      userDetails.meetupLocations = meetupLocations;

      const locationCollection: ILocation[] = [{ id: 7244 }];
      jest.spyOn(locationService, 'query').mockReturnValue(of(new HttpResponse({ body: locationCollection })));
      const additionalLocations = [...meetupLocations];
      const expectedCollection: ILocation[] = [...additionalLocations, ...locationCollection];
      jest.spyOn(locationService, 'addLocationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ userDetails });
      comp.ngOnInit();

      expect(locationService.query).toHaveBeenCalled();
      expect(locationService.addLocationToCollectionIfMissing).toHaveBeenCalledWith(
        locationCollection,
        ...additionalLocations.map(expect.objectContaining),
      );
      expect(comp.locationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Conversation query and add missing value', () => {
      const userDetails: IUserDetails = { id: 456 };
      const chats: IConversation[] = [{ id: 29217 }];
      userDetails.chats = chats;

      const conversationCollection: IConversation[] = [{ id: 15892 }];
      jest.spyOn(conversationService, 'query').mockReturnValue(of(new HttpResponse({ body: conversationCollection })));
      const additionalConversations = [...chats];
      const expectedCollection: IConversation[] = [...additionalConversations, ...conversationCollection];
      jest.spyOn(conversationService, 'addConversationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ userDetails });
      comp.ngOnInit();

      expect(conversationService.query).toHaveBeenCalled();
      expect(conversationService.addConversationToCollectionIfMissing).toHaveBeenCalledWith(
        conversationCollection,
        ...additionalConversations.map(expect.objectContaining),
      );
      expect(comp.conversationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const userDetails: IUserDetails = { id: 456 };
      const user: IUser = { id: 16918 };
      userDetails.user = user;
      const meetupLocations: ILocation = { id: 13772 };
      userDetails.meetupLocations = [meetupLocations];
      const chats: IConversation = { id: 18637 };
      userDetails.chats = [chats];

      activatedRoute.data = of({ userDetails });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.locationsSharedCollection).toContain(meetupLocations);
      expect(comp.conversationsSharedCollection).toContain(chats);
      expect(comp.userDetails).toEqual(userDetails);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserDetails>>();
      const userDetails = { id: 123 };
      jest.spyOn(userDetailsFormService, 'getUserDetails').mockReturnValue(userDetails);
      jest.spyOn(userDetailsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userDetails }));
      saveSubject.complete();

      // THEN
      expect(userDetailsFormService.getUserDetails).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(userDetailsService.update).toHaveBeenCalledWith(expect.objectContaining(userDetails));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserDetails>>();
      const userDetails = { id: 123 };
      jest.spyOn(userDetailsFormService, 'getUserDetails').mockReturnValue({ id: null });
      jest.spyOn(userDetailsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userDetails: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userDetails }));
      saveSubject.complete();

      // THEN
      expect(userDetailsFormService.getUserDetails).toHaveBeenCalled();
      expect(userDetailsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserDetails>>();
      const userDetails = { id: 123 };
      jest.spyOn(userDetailsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(userDetailsService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
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

    describe('compareConversation', () => {
      it('Should forward to conversationService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(conversationService, 'compareConversation');
        comp.compareConversation(entity, entity2);
        expect(conversationService.compareConversation).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
