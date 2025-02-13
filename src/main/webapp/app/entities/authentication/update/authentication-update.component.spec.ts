import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { AuthenticationService } from '../service/authentication.service';
import { IAuthentication } from '../authentication.model';
import { AuthenticationFormService } from './authentication-form.service';

import { AuthenticationUpdateComponent } from './authentication-update.component';

describe('Authentication Management Update Component', () => {
  let comp: AuthenticationUpdateComponent;
  let fixture: ComponentFixture<AuthenticationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let authenticationFormService: AuthenticationFormService;
  let authenticationService: AuthenticationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AuthenticationUpdateComponent],
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
      .overrideTemplate(AuthenticationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AuthenticationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    authenticationFormService = TestBed.inject(AuthenticationFormService);
    authenticationService = TestBed.inject(AuthenticationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const authentication: IAuthentication = { id: 456 };

      activatedRoute.data = of({ authentication });
      comp.ngOnInit();

      expect(comp.authentication).toEqual(authentication);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAuthentication>>();
      const authentication = { id: 123 };
      jest.spyOn(authenticationFormService, 'getAuthentication').mockReturnValue(authentication);
      jest.spyOn(authenticationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ authentication });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: authentication }));
      saveSubject.complete();

      // THEN
      expect(authenticationFormService.getAuthentication).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(authenticationService.update).toHaveBeenCalledWith(expect.objectContaining(authentication));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAuthentication>>();
      const authentication = { id: 123 };
      jest.spyOn(authenticationFormService, 'getAuthentication').mockReturnValue({ id: null });
      jest.spyOn(authenticationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ authentication: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: authentication }));
      saveSubject.complete();

      // THEN
      expect(authenticationFormService.getAuthentication).toHaveBeenCalled();
      expect(authenticationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAuthentication>>();
      const authentication = { id: 123 };
      jest.spyOn(authenticationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ authentication });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(authenticationService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
