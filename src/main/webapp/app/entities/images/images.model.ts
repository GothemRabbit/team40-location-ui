import { IItem } from 'app/entities/item/item.model';

export interface IImages {
  id: number;
  images?: string | null;
  imagesContentType?: string | null;
  item?: Pick<IItem, 'id'> | null;
}

export type NewImages = Omit<IImages, 'id'> & { id: null };
