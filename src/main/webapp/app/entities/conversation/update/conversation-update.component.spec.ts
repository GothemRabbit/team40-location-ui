import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IUserDetails } from 'app/entities/user-details/user-details.model';
import { UserDetailsService } from 'app/entities/user-details/service/user-details.service';
import { ConversationService } from '../service/conversation.service';
import { IConversation } from '../conversation.model';
import { ConversationFormService } from './conversation-form.service';

import { ConversationUpdateComponent } from './conversation-update.component';

describe('Conversation Management Update Component', () => {
  let comp: ConversationUpdateComponent;
  let fixture: ComponentFixture<ConversationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let conversationFormService: ConversationFormService;
  let conversationService: ConversationService;
  let userDetailsService: UserDetailsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ConversationUpdateComponent],
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
      .overrideTemplate(ConversationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ConversationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    conversationFormService = TestBed.inject(ConversationFormService);
    conversationService = TestBed.inject(ConversationService);
    userDetailsService = TestBed.inject(UserDetailsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call UserDetails query and add missing value', () => {
      const conversation: IConversation = { id: 456 };
      const userOne: IUserDetails = { id: 2989 };
      conversation.userOne = userOne;
      const userTwo: IUserDetails = { id: 26772 };
      conversation.userTwo = userTwo;

      const userDetailsCollection: IUserDetails[] = [{ id: 6580 }];
      jest.spyOn(userDetailsService, 'query').mockReturnValue(of(new HttpResponse({ body: userDetailsCollection })));
      const additionalUserDetails = [userOne, userTwo];
      const expectedCollection: IUserDetails[] = [...additionalUserDetails, ...userDetailsCollection];
      jest.spyOn(userDetailsService, 'addUserDetailsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ conversation });
      comp.ngOnInit();

      expect(userDetailsService.query).toHaveBeenCalled();
      expect(userDetailsService.addUserDetailsToCollectionIfMissing).toHaveBeenCalledWith(
        userDetailsCollection,
        ...additionalUserDetails.map(expect.objectContaining),
      );
      expect(comp.userDetailsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const conversation: IConversation = { id: 456 };
      const userOne: IUserDetails = { id: 12142 };
      conversation.userOne = userOne;
      const userTwo: IUserDetails = { id: 28578 };
      conversation.userTwo = userTwo;

      activatedRoute.data = of({ conversation });
      comp.ngOnInit();

      expect(comp.userDetailsSharedCollection).toContain(userOne);
      expect(comp.userDetailsSharedCollection).toContain(userTwo);
      expect(comp.conversation).toEqual(conversation);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConversation>>();
      const conversation = { id: 123 };
      jest.spyOn(conversationFormService, 'getConversation').mockReturnValue(conversation);
      jest.spyOn(conversationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ conversation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: conversation }));
      saveSubject.complete();

      // THEN
      expect(conversationFormService.getConversation).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(conversationService.update).toHaveBeenCalledWith(expect.objectContaining(conversation));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConversation>>();
      const conversation = { id: 123 };
      jest.spyOn(conversationFormService, 'getConversation').mockReturnValue({ id: null });
      jest.spyOn(conversationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ conversation: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: conversation }));
      saveSubject.complete();

      // THEN
      expect(conversationFormService.getConversation).toHaveBeenCalled();
      expect(conversationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IConversation>>();
      const conversation = { id: 123 };
      jest.spyOn(conversationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ conversation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(conversationService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
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
