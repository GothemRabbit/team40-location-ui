import dayjs from 'dayjs/esm';

import { IItem, NewItem } from './item.model';

export const sampleWithRequiredData: IItem = {
  id: 26745,
  itemTitle: 'to',
  itemId: 12856,
  itemPrice: 988.02,
  itemCondition: 'BRANDNEW',
  itemCategory: 'OTHERS',
  itemImage: '../fake-data/blob/hipster.png',
  itemImageContentType: 'unknown',
  timeListed: dayjs('2025-02-12T17:11'),
};

export const sampleWithPartialData: IItem = {
  id: 3176,
  itemTitle: 'while',
  itemId: 24178,
  itemPrice: 21051.98,
  itemCondition: 'FAIRLYUSED',
  itemCategory: 'CLOTHING',
  itemColour: 'a vein',
  itemImage: '../fake-data/blob/hipster.png',
  itemImageContentType: 'unknown',
  timeListed: dayjs('2025-02-12T13:29'),
};

export const sampleWithFullData: IItem = {
  id: 4358,
  itemTitle: 'lampoon by',
  itemId: 30513,
  itemPrice: 18608.46,
  itemSize: 'reasoning',
  itemCondition: 'WELLUSED',
  itemCategory: 'ELECTRONICS',
  description: '../fake-data/blob/hipster.txt',
  itemColour: 'ah',
  itemImage: '../fake-data/blob/hipster.png',
  itemImageContentType: 'unknown',
  timeListed: dayjs('2025-02-13T04:42'),
};

export const sampleWithNewData: NewItem = {
  itemTitle: 'notwithstanding boo incidentally',
  itemId: 8506,
  itemPrice: 21660.95,
  itemCondition: 'LIKENEW',
  itemCategory: 'HEADWEAR',
  itemImage: '../fake-data/blob/hipster.png',
  itemImageContentType: 'unknown',
  timeListed: dayjs('2025-02-13T08:48'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
