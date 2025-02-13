import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

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

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const userDetails: IUserDetails = { id: 456 };

      activatedRoute.data = of({ userDetails });
      comp.ngOnInit();

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
});
