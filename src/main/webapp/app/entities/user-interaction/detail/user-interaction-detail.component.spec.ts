import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { UserInteractionDetailComponent } from './user-interaction-detail.component';

describe('UserInteraction Management Detail Component', () => {
  let comp: UserInteractionDetailComponent;
  let fixture: ComponentFixture<UserInteractionDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserInteractionDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./user-interaction-detail.component').then(m => m.UserInteractionDetailComponent),
              resolve: { userInteraction: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(UserInteractionDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UserInteractionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load userInteraction on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', UserInteractionDetailComponent);

      // THEN
      expect(instance.userInteraction()).toEqual(expect.objectContaining({ id: 123 }));
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
