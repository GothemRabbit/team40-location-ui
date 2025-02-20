import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { UserInteractionService } from '../service/user-interaction.service';
import { IUserInteraction } from '../user-interaction.model';
import { UserInteractionFormService } from './user-interaction-form.service';

import { UserInteractionUpdateComponent } from './user-interaction-update.component';

describe('UserInteraction Management Update Component', () => {
  let comp: UserInteractionUpdateComponent;
  let fixture: ComponentFixture<UserInteractionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let userInteractionFormService: UserInteractionFormService;
  let userInteractionService: UserInteractionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [UserInteractionUpdateComponent],
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
      .overrideTemplate(UserInteractionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UserInteractionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    userInteractionFormService = TestBed.inject(UserInteractionFormService);
    userInteractionService = TestBed.inject(UserInteractionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const userInteraction: IUserInteraction = { id: 456 };

      activatedRoute.data = of({ userInteraction });
      comp.ngOnInit();

      expect(comp.userInteraction).toEqual(userInteraction);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserInteraction>>();
      const userInteraction = { id: 123 };
      jest.spyOn(userInteractionFormService, 'getUserInteraction').mockReturnValue(userInteraction);
      jest.spyOn(userInteractionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userInteraction });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userInteraction }));
      saveSubject.complete();

      // THEN
      expect(userInteractionFormService.getUserInteraction).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(userInteractionService.update).toHaveBeenCalledWith(expect.objectContaining(userInteraction));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserInteraction>>();
      const userInteraction = { id: 123 };
      jest.spyOn(userInteractionFormService, 'getUserInteraction').mockReturnValue({ id: null });
      jest.spyOn(userInteractionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userInteraction: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userInteraction }));
      saveSubject.complete();

      // THEN
      expect(userInteractionFormService.getUserInteraction).toHaveBeenCalled();
      expect(userInteractionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserInteraction>>();
      const userInteraction = { id: 123 };
      jest.spyOn(userInteractionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userInteraction });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(userInteractionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
