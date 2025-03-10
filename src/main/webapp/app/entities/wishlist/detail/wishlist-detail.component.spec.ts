import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { WishlistDetailComponent } from './wishlist-detail.component';

describe('Wishlist Management Detail Component', () => {
  let comp: WishlistDetailComponent;
  let fixture: ComponentFixture<WishlistDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WishlistDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./wishlist-detail.component').then(m => m.WishlistDetailComponent),
              resolve: { wishlist: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(WishlistDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(WishlistDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load wishlist on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', WishlistDetailComponent);

      // THEN
      expect(instance.wishlist()).toEqual(expect.objectContaining({ id: 123 }));
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
