import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IItem } from 'app/entities/item/item.model';
import { ItemService } from 'app/entities/item/service/item.service';
import { ImagesService } from '../service/images.service';
import { IImages } from '../images.model';
import { ImagesFormService } from './images-form.service';

import { ImagesUpdateComponent } from './images-update.component';

describe('Images Management Update Component', () => {
  let comp: ImagesUpdateComponent;
  let fixture: ComponentFixture<ImagesUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let imagesFormService: ImagesFormService;
  let imagesService: ImagesService;
  let itemService: ItemService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ImagesUpdateComponent],
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
      .overrideTemplate(ImagesUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ImagesUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    imagesFormService = TestBed.inject(ImagesFormService);
    imagesService = TestBed.inject(ImagesService);
    itemService = TestBed.inject(ItemService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Item query and add missing value', () => {
      const images: IImages = { id: 456 };
      const item: IItem = { id: 5175 };
      images.item = item;

      const itemCollection: IItem[] = [{ id: 5324 }];
      jest.spyOn(itemService, 'query').mockReturnValue(of(new HttpResponse({ body: itemCollection })));
      const additionalItems = [item];
      const expectedCollection: IItem[] = [...additionalItems, ...itemCollection];
      jest.spyOn(itemService, 'addItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ images });
      comp.ngOnInit();

      expect(itemService.query).toHaveBeenCalled();
      expect(itemService.addItemToCollectionIfMissing).toHaveBeenCalledWith(
        itemCollection,
        ...additionalItems.map(expect.objectContaining),
      );
      expect(comp.itemsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const images: IImages = { id: 456 };
      const item: IItem = { id: 1189 };
      images.item = item;

      activatedRoute.data = of({ images });
      comp.ngOnInit();

      expect(comp.itemsSharedCollection).toContain(item);
      expect(comp.images).toEqual(images);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IImages>>();
      const images = { id: 123 };
      jest.spyOn(imagesFormService, 'getImages').mockReturnValue(images);
      jest.spyOn(imagesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ images });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: images }));
      saveSubject.complete();

      // THEN
      expect(imagesFormService.getImages).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(imagesService.update).toHaveBeenCalledWith(expect.objectContaining(images));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IImages>>();
      const images = { id: 123 };
      jest.spyOn(imagesFormService, 'getImages').mockReturnValue({ id: null });
      jest.spyOn(imagesService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ images: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: images }));
      saveSubject.complete();

      // THEN
      expect(imagesFormService.getImages).toHaveBeenCalled();
      expect(imagesService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IImages>>();
      const images = { id: 123 };
      jest.spyOn(imagesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ images });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(imagesService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareItem', () => {
      it('Should forward to itemService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(itemService, 'compareItem');
        comp.compareItem(entity, entity2);
        expect(itemService.compareItem).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
