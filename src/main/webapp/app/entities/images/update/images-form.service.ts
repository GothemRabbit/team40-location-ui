import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IImages, NewImages } from '../images.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IImages for edit and NewImagesFormGroupInput for create.
 */
type ImagesFormGroupInput = IImages | PartialWithRequiredKeyOf<NewImages>;

type ImagesFormDefaults = Pick<NewImages, 'id'>;

type ImagesFormGroupContent = {
  id: FormControl<IImages['id'] | NewImages['id']>;
  images: FormControl<IImages['images']>;
  imagesContentType: FormControl<IImages['imagesContentType']>;
  item: FormControl<IImages['item']>;
};

export type ImagesFormGroup = FormGroup<ImagesFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ImagesFormService {
  createImagesFormGroup(images: ImagesFormGroupInput = { id: null }): ImagesFormGroup {
    const imagesRawValue = {
      ...this.getFormDefaults(),
      ...images,
    };
    return new FormGroup<ImagesFormGroupContent>({
      id: new FormControl(
        { value: imagesRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      images: new FormControl(imagesRawValue.images, {
        validators: [Validators.required],
      }),
      imagesContentType: new FormControl(imagesRawValue.imagesContentType),
      item: new FormControl(imagesRawValue.item),
    });
  }

  getImages(form: ImagesFormGroup): IImages | NewImages {
    return form.getRawValue() as IImages | NewImages;
  }

  resetForm(form: ImagesFormGroup, images: ImagesFormGroupInput): void {
    const imagesRawValue = { ...this.getFormDefaults(), ...images };
    form.reset(
      {
        ...imagesRawValue,
        id: { value: imagesRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ImagesFormDefaults {
    return {
      id: null,
    };
  }
}
