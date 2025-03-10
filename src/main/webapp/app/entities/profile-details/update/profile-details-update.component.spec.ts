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
import { IProfileDetails } from '../profile-details.model';
import { ProfileDetailsService } from '../service/profile-details.service';
import { ProfileDetailsFormService } from './profile-details-form.service';

import { ProfileDetailsUpdateComponent } from './profile-details-update.component';

describe('ProfileDetails Management Update Component', () => {
  let comp: ProfileDetailsUpdateComponent;
  let fixture: ComponentFixture<ProfileDetailsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let profileDetailsFormService: ProfileDetailsFormService;
  let profileDetailsService: ProfileDetailsService;
  let userService: UserService;
  let locationService: LocationService;
  let conversationService: ConversationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ProfileDetailsUpdateComponent],
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
      .overrideTemplate(ProfileDetailsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProfileDetailsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    profileDetailsFormService = TestBed.inject(ProfileDetailsFormService);
    profileDetailsService = TestBed.inject(ProfileDetailsService);
    userService = TestBed.inject(UserService);
    locationService = TestBed.inject(LocationService);
    conversationService = TestBed.inject(ConversationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const profileDetails: IProfileDetails = { id: 456 };
      const user: IUser = { id: 13009 };
      profileDetails.user = user;

      const userCollection: IUser[] = [{ id: 14649 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ profileDetails });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Location query and add missing value', () => {
      const profileDetails: IProfileDetails = { id: 456 };
      const locations: ILocation[] = [{ id: 5016 }];
      profileDetails.locations = locations;

      const locationCollection: ILocation[] = [{ id: 1179 }];
      jest.spyOn(locationService, 'query').mockReturnValue(of(new HttpResponse({ body: locationCollection })));
      const additionalLocations = [...locations];
      const expectedCollection: ILocation[] = [...additionalLocations, ...locationCollection];
      jest.spyOn(locationService, 'addLocationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ profileDetails });
      comp.ngOnInit();

      expect(locationService.query).toHaveBeenCalled();
      expect(locationService.addLocationToCollectionIfMissing).toHaveBeenCalledWith(
        locationCollection,
        ...additionalLocations.map(expect.objectContaining),
      );
      expect(comp.locationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Conversation query and add missing value', () => {
      const profileDetails: IProfileDetails = { id: 456 };
      const conversations: IConversation[] = [{ id: 8835 }];
      profileDetails.conversations = conversations;

      const conversationCollection: IConversation[] = [{ id: 24370 }];
      jest.spyOn(conversationService, 'query').mockReturnValue(of(new HttpResponse({ body: conversationCollection })));
      const additionalConversations = [...conversations];
      const expectedCollection: IConversation[] = [...additionalConversations, ...conversationCollection];
      jest.spyOn(conversationService, 'addConversationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ profileDetails });
      comp.ngOnInit();

      expect(conversationService.query).toHaveBeenCalled();
      expect(conversationService.addConversationToCollectionIfMissing).toHaveBeenCalledWith(
        conversationCollection,
        ...additionalConversations.map(expect.objectContaining),
      );
      expect(comp.conversationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const profileDetails: IProfileDetails = { id: 456 };
      const user: IUser = { id: 12978 };
      profileDetails.user = user;
      const location: ILocation = { id: 8990 };
      profileDetails.locations = [location];
      const conversation: IConversation = { id: 150 };
      profileDetails.conversations = [conversation];

      activatedRoute.data = of({ profileDetails });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.locationsSharedCollection).toContain(location);
      expect(comp.conversationsSharedCollection).toContain(conversation);
      expect(comp.profileDetails).toEqual(profileDetails);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProfileDetails>>();
      const profileDetails = { id: 123 };
      jest.spyOn(profileDetailsFormService, 'getProfileDetails').mockReturnValue(profileDetails);
      jest.spyOn(profileDetailsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ profileDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: profileDetails }));
      saveSubject.complete();

      // THEN
      expect(profileDetailsFormService.getProfileDetails).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(profileDetailsService.update).toHaveBeenCalledWith(expect.objectContaining(profileDetails));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProfileDetails>>();
      const profileDetails = { id: 123 };
      jest.spyOn(profileDetailsFormService, 'getProfileDetails').mockReturnValue({ id: null });
      jest.spyOn(profileDetailsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ profileDetails: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: profileDetails }));
      saveSubject.complete();

      // THEN
      expect(profileDetailsFormService.getProfileDetails).toHaveBeenCalled();
      expect(profileDetailsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IProfileDetails>>();
      const profileDetails = { id: 123 };
      jest.spyOn(profileDetailsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ profileDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(profileDetailsService.update).toHaveBeenCalled();
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
