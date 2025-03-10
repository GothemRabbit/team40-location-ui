import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IConversation } from 'app/entities/conversation/conversation.model';
import { ConversationService } from 'app/entities/conversation/service/conversation.service';
import { IProfileDetails } from 'app/entities/profile-details/profile-details.model';
import { ProfileDetailsService } from 'app/entities/profile-details/service/profile-details.service';
import { IMessage } from '../message.model';
import { MessageService } from '../service/message.service';
import { MessageFormService } from './message-form.service';

import { MessageUpdateComponent } from './message-update.component';

describe('Message Management Update Component', () => {
  let comp: MessageUpdateComponent;
  let fixture: ComponentFixture<MessageUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let messageFormService: MessageFormService;
  let messageService: MessageService;
  let conversationService: ConversationService;
  let profileDetailsService: ProfileDetailsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [MessageUpdateComponent],
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
      .overrideTemplate(MessageUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MessageUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    messageFormService = TestBed.inject(MessageFormService);
    messageService = TestBed.inject(MessageService);
    conversationService = TestBed.inject(ConversationService);
    profileDetailsService = TestBed.inject(ProfileDetailsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Conversation query and add missing value', () => {
      const message: IMessage = { id: 456 };
      const conversation: IConversation = { id: 27041 };
      message.conversation = conversation;

      const conversationCollection: IConversation[] = [{ id: 21260 }];
      jest.spyOn(conversationService, 'query').mockReturnValue(of(new HttpResponse({ body: conversationCollection })));
      const additionalConversations = [conversation];
      const expectedCollection: IConversation[] = [...additionalConversations, ...conversationCollection];
      jest.spyOn(conversationService, 'addConversationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ message });
      comp.ngOnInit();

      expect(conversationService.query).toHaveBeenCalled();
      expect(conversationService.addConversationToCollectionIfMissing).toHaveBeenCalledWith(
        conversationCollection,
        ...additionalConversations.map(expect.objectContaining),
      );
      expect(comp.conversationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ProfileDetails query and add missing value', () => {
      const message: IMessage = { id: 456 };
      const profileDetails: IProfileDetails = { id: 26575 };
      message.profileDetails = profileDetails;

      const profileDetailsCollection: IProfileDetails[] = [{ id: 14461 }];
      jest.spyOn(profileDetailsService, 'query').mockReturnValue(of(new HttpResponse({ body: profileDetailsCollection })));
      const additionalProfileDetails = [profileDetails];
      const expectedCollection: IProfileDetails[] = [...additionalProfileDetails, ...profileDetailsCollection];
      jest.spyOn(profileDetailsService, 'addProfileDetailsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ message });
      comp.ngOnInit();

      expect(profileDetailsService.query).toHaveBeenCalled();
      expect(profileDetailsService.addProfileDetailsToCollectionIfMissing).toHaveBeenCalledWith(
        profileDetailsCollection,
        ...additionalProfileDetails.map(expect.objectContaining),
      );
      expect(comp.profileDetailsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const message: IMessage = { id: 456 };
      const conversation: IConversation = { id: 15198 };
      message.conversation = conversation;
      const profileDetails: IProfileDetails = { id: 27250 };
      message.profileDetails = profileDetails;

      activatedRoute.data = of({ message });
      comp.ngOnInit();

      expect(comp.conversationsSharedCollection).toContain(conversation);
      expect(comp.profileDetailsSharedCollection).toContain(profileDetails);
      expect(comp.message).toEqual(message);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMessage>>();
      const message = { id: 123 };
      jest.spyOn(messageFormService, 'getMessage').mockReturnValue(message);
      jest.spyOn(messageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ message });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: message }));
      saveSubject.complete();

      // THEN
      expect(messageFormService.getMessage).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(messageService.update).toHaveBeenCalledWith(expect.objectContaining(message));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMessage>>();
      const message = { id: 123 };
      jest.spyOn(messageFormService, 'getMessage').mockReturnValue({ id: null });
      jest.spyOn(messageService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ message: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: message }));
      saveSubject.complete();

      // THEN
      expect(messageFormService.getMessage).toHaveBeenCalled();
      expect(messageService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMessage>>();
      const message = { id: 123 };
      jest.spyOn(messageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ message });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(messageService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
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
  });
});
