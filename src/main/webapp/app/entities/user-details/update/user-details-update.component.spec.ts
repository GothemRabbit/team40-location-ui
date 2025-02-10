import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IConversation } from 'app/entities/conversation/conversation.model';
import { ConversationService } from 'app/entities/conversation/service/conversation.service';
import { UserDetailsService } from '../service/user-details.service';
import { IUserDetails } from '../user-details.model';
import { UserDetailsFormService } from './user-details-form.service';

import { UserDetailsUpdateComponent } from './user-details-update.component';

describe('UserDetails Management Update Component', () => {
  let comp: UserDetailsUpdateComponent;
  let fixture: ComponentFixture<UserDetailsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let userDetailsFormService: UserDetailsFormService;
  let userDetailsService: UserDetailsService;
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
    conversationService = TestBed.inject(ConversationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Conversation query and add missing value', () => {
      const userDetails: IUserDetails = { id: 456 };
      const conversations: IConversation[] = [{ id: 13694 }];
      userDetails.conversations = conversations;

      const conversationCollection: IConversation[] = [{ id: 15740 }];
      jest.spyOn(conversationService, 'query').mockReturnValue(of(new HttpResponse({ body: conversationCollection })));
      const additionalConversations = [...conversations];
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
      const conversation: IConversation = { id: 16917 };
      userDetails.conversations = [conversation];

      activatedRoute.data = of({ userDetails });
      comp.ngOnInit();

      expect(comp.conversationsSharedCollection).toContain(conversation);
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
