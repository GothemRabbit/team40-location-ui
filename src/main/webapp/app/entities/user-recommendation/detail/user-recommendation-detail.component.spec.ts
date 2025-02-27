import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { UserRecommendationDetailComponent } from './user-recommendation-detail.component';

describe('UserRecommendation Management Detail Component', () => {
  let comp: UserRecommendationDetailComponent;
  let fixture: ComponentFixture<UserRecommendationDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserRecommendationDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./user-recommendation-detail.component').then(m => m.UserRecommendationDetailComponent),
              resolve: { userRecommendation: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(UserRecommendationDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UserRecommendationDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load userRecommendation on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', UserRecommendationDetailComponent);

      // THEN
      expect(instance.userRecommendation()).toEqual(expect.objectContaining({ id: 123 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
