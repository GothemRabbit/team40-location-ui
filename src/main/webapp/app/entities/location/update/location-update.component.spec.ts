import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IUserDetails } from 'app/entities/user-details/user-details.model';
import { UserDetailsService } from 'app/entities/user-details/service/user-details.service';
import { LocationService } from '../service/location.service';
import { ILocation } from '../location.model';
import { LocationFormService } from './location-form.service';

import { LocationUpdateComponent } from './location-update.component';

describe('Location Management Update Component', () => {
  let comp: LocationUpdateComponent;
  let fixture: ComponentFixture<LocationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let locationFormService: LocationFormService;
  let locationService: LocationService;
  let userDetailsService: UserDetailsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [LocationUpdateComponent],
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
      .overrideTemplate(LocationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(LocationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    locationFormService = TestBed.inject(LocationFormService);
    locationService = TestBed.inject(LocationService);
    userDetailsService = TestBed.inject(UserDetailsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call UserDetails query and add missing value', () => {
      const location: ILocation = { id: 456 };
      const users: IUserDetails[] = [{ id: 30110 }];
      location.users = users;

      const userDetailsCollection: IUserDetails[] = [{ id: 7220 }];
      jest.spyOn(userDetailsService, 'query').mockReturnValue(of(new HttpResponse({ body: userDetailsCollection })));
      const additionalUserDetails = [...users];
      const expectedCollection: IUserDetails[] = [...additionalUserDetails, ...userDetailsCollection];
      jest.spyOn(userDetailsService, 'addUserDetailsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ location });
      comp.ngOnInit();

      expect(userDetailsService.query).toHaveBeenCalled();
      expect(userDetailsService.addUserDetailsToCollectionIfMissing).toHaveBeenCalledWith(
        userDetailsCollection,
        ...additionalUserDetails.map(expect.objectContaining),
      );
      expect(comp.userDetailsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const location: ILocation = { id: 456 };
      const users: IUserDetails = { id: 21259 };
      location.users = [users];

      activatedRoute.data = of({ location });
      comp.ngOnInit();

      expect(comp.userDetailsSharedCollection).toContain(users);
      expect(comp.location).toEqual(location);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILocation>>();
      const location = { id: 123 };
      jest.spyOn(locationFormService, 'getLocation').mockReturnValue(location);
      jest.spyOn(locationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ location });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: location }));
      saveSubject.complete();

      // THEN
      expect(locationFormService.getLocation).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(locationService.update).toHaveBeenCalledWith(expect.objectContaining(location));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILocation>>();
      const location = { id: 123 };
      jest.spyOn(locationFormService, 'getLocation').mockReturnValue({ id: null });
      jest.spyOn(locationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ location: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: location }));
      saveSubject.complete();

      // THEN
      expect(locationFormService.getLocation).toHaveBeenCalled();
      expect(locationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ILocation>>();
      const location = { id: 123 };
      jest.spyOn(locationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ location });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(locationService.update).toHaveBeenCalled();
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
