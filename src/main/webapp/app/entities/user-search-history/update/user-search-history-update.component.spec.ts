import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { UserSearchHistoryService } from '../service/user-search-history.service';
import { IUserSearchHistory } from '../user-search-history.model';
import { UserSearchHistoryFormService } from './user-search-history-form.service';

import { UserSearchHistoryUpdateComponent } from './user-search-history-update.component';

describe('UserSearchHistory Management Update Component', () => {
  let comp: UserSearchHistoryUpdateComponent;
  let fixture: ComponentFixture<UserSearchHistoryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let userSearchHistoryFormService: UserSearchHistoryFormService;
  let userSearchHistoryService: UserSearchHistoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [UserSearchHistoryUpdateComponent],
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
      .overrideTemplate(UserSearchHistoryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UserSearchHistoryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    userSearchHistoryFormService = TestBed.inject(UserSearchHistoryFormService);
    userSearchHistoryService = TestBed.inject(UserSearchHistoryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const userSearchHistory: IUserSearchHistory = { id: 456 };

      activatedRoute.data = of({ userSearchHistory });
      comp.ngOnInit();

      expect(comp.userSearchHistory).toEqual(userSearchHistory);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserSearchHistory>>();
      const userSearchHistory = { id: 123 };
      jest.spyOn(userSearchHistoryFormService, 'getUserSearchHistory').mockReturnValue(userSearchHistory);
      jest.spyOn(userSearchHistoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userSearchHistory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userSearchHistory }));
      saveSubject.complete();

      // THEN
      expect(userSearchHistoryFormService.getUserSearchHistory).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(userSearchHistoryService.update).toHaveBeenCalledWith(expect.objectContaining(userSearchHistory));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserSearchHistory>>();
      const userSearchHistory = { id: 123 };
      jest.spyOn(userSearchHistoryFormService, 'getUserSearchHistory').mockReturnValue({ id: null });
      jest.spyOn(userSearchHistoryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userSearchHistory: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userSearchHistory }));
      saveSubject.complete();

      // THEN
      expect(userSearchHistoryFormService.getUserSearchHistory).toHaveBeenCalled();
      expect(userSearchHistoryService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserSearchHistory>>();
      const userSearchHistory = { id: 123 };
      jest.spyOn(userSearchHistoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userSearchHistory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(userSearchHistoryService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
