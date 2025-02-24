import dayjs from 'dayjs/esm';

import { IItem, NewItem } from './item.model';

export const sampleWithRequiredData: IItem = {
  id: 25398,
  itemTitle: 'replacement phew',
  itemPrice: 18234.14,
  itemCondition: 'LIKENEW',
  itemCategory: 'OTHERS',
  itemImage: '../fake-data/blob/hipster.png',
  itemImageContentType: 'unknown',
  timeListed: dayjs('2025-02-13T02:57'),
};

export const sampleWithPartialData: IItem = {
  id: 14040,
  itemTitle: 'whenever unless bashfully',
  itemPrice: 572.62,
  itemSize: 'sock',
  itemCondition: 'HEAVILYUSED',
  itemCategory: 'FOOTWEAR',
  description: '../fake-data/blob/hipster.txt',
  itemImage: '../fake-data/blob/hipster.png',
  itemImageContentType: 'unknown',
  timeListed: dayjs('2025-02-12T14:16'),
};

export const sampleWithFullData: IItem = {
  id: 808,
  itemTitle: 'brr cope',
  itemPrice: 4410.79,
  itemSize: 'ouch modulo less',
  itemCondition: 'FAIRLYUSED',
  itemCategory: 'OTHERS',
  description: '../fake-data/blob/hipster.txt',
  itemColour: 'unethically restfully',
  itemImage: '../fake-data/blob/hipster.png',
  itemImageContentType: 'unknown',
  timeListed: dayjs('2025-02-13T05:18'),
  itemLike: true,
};

export const sampleWithNewData: NewItem = {
  itemTitle: 'extra-large',
  itemPrice: 17831.37,
  itemCondition: 'FAIRLYUSED',
  itemCategory: 'HEADWEAR',
  itemImage: '../fake-data/blob/hipster.png',
  itemImageContentType: 'unknown',
  timeListed: dayjs('2025-02-12T12:40'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
