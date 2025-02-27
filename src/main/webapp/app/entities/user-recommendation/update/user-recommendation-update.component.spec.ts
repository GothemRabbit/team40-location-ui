import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { UserRecommendationService } from '../service/user-recommendation.service';
import { IUserRecommendation } from '../user-recommendation.model';
import { UserRecommendationFormService } from './user-recommendation-form.service';

import { UserRecommendationUpdateComponent } from './user-recommendation-update.component';

describe('UserRecommendation Management Update Component', () => {
  let comp: UserRecommendationUpdateComponent;
  let fixture: ComponentFixture<UserRecommendationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let userRecommendationFormService: UserRecommendationFormService;
  let userRecommendationService: UserRecommendationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [UserRecommendationUpdateComponent],
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
      .overrideTemplate(UserRecommendationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UserRecommendationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    userRecommendationFormService = TestBed.inject(UserRecommendationFormService);
    userRecommendationService = TestBed.inject(UserRecommendationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const userRecommendation: IUserRecommendation = { id: 456 };

      activatedRoute.data = of({ userRecommendation });
      comp.ngOnInit();

      expect(comp.userRecommendation).toEqual(userRecommendation);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserRecommendation>>();
      const userRecommendation = { id: 123 };
      jest.spyOn(userRecommendationFormService, 'getUserRecommendation').mockReturnValue(userRecommendation);
      jest.spyOn(userRecommendationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userRecommendation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userRecommendation }));
      saveSubject.complete();

      // THEN
      expect(userRecommendationFormService.getUserRecommendation).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(userRecommendationService.update).toHaveBeenCalledWith(expect.objectContaining(userRecommendation));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserRecommendation>>();
      const userRecommendation = { id: 123 };
      jest.spyOn(userRecommendationFormService, 'getUserRecommendation').mockReturnValue({ id: null });
      jest.spyOn(userRecommendationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userRecommendation: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userRecommendation }));
      saveSubject.complete();

      // THEN
      expect(userRecommendationFormService.getUserRecommendation).toHaveBeenCalled();
      expect(userRecommendationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserRecommendation>>();
      const userRecommendation = { id: 123 };
      jest.spyOn(userRecommendationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userRecommendation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(userRecommendationService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
