import dayjs from 'dayjs/esm';
import { Condition } from 'app/entities/enumerations/condition.model';
import { Category } from 'app/entities/enumerations/category.model';

export interface IItem {
  id: number;
  itemTitle?: string | null;
  itemPrice?: number | null;
  itemSize?: string | null;
  itemCondition?: keyof typeof Condition | null;
  itemCategory?: keyof typeof Category | null;
  description?: string | null;
  itemColour?: string | null;
  itemImage?: string | null;
  itemImageContentType?: string | null;
  timeListed?: dayjs.Dayjs | null;
  itemLike?: boolean | null;
  dropDown?: boolean | null;
  //itemType?: keyof typeof Type | null;
  //itemBrand?: string | null;
}

export type NewItem = Omit<IItem, 'id'> & { id: null };
