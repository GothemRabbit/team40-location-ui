import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ILikes, NewLikes } from '../likes.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ILikes for edit and NewLikesFormGroupInput for create.
 */
type LikesFormGroupInput = ILikes | PartialWithRequiredKeyOf<NewLikes>;

type LikesFormDefaults = Pick<NewLikes, 'id' | 'liked'>;

type LikesFormGroupContent = {
  id: FormControl<ILikes['id'] | NewLikes['id']>;
  liked: FormControl<ILikes['liked']>;
  item: FormControl<ILikes['item']>;
  user: FormControl<ILikes['user']>;
};

export type LikesFormGroup = FormGroup<LikesFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class LikesFormService {
  createLikesFormGroup(likes: LikesFormGroupInput = { id: null }): LikesFormGroup {
    const likesRawValue = {
      ...this.getFormDefaults(),
      ...likes,
    };
    return new FormGroup<LikesFormGroupContent>({
      id: new FormControl(
        { value: likesRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      liked: new FormControl(likesRawValue.liked),
      item: new FormControl(likesRawValue.item),
      user: new FormControl(likesRawValue.user),
    });
  }

  getLikes(form: LikesFormGroup): ILikes | NewLikes {
    return form.getRawValue() as ILikes | NewLikes;
  }

  resetForm(form: LikesFormGroup, likes: LikesFormGroupInput): void {
    const likesRawValue = { ...this.getFormDefaults(), ...likes };
    form.reset(
      {
        ...likesRawValue,
        id: { value: likesRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): LikesFormDefaults {
    return {
      id: null,
      liked: false,
    };
  }
}
