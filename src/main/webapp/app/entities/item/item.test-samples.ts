import dayjs from 'dayjs/esm';

import { IItem, NewItem } from './item.model';

export const sampleWithRequiredData: IItem = {
  id: 15582,
  itemTitle: 'right',
  itemPrice: 12739.48,
  itemCondition: 'FAIRLYUSED',
  itemCategory: 'OTHERS',
  itemImage: '../fake-data/blob/hipster.png',
  itemImageContentType: 'unknown',
  timeListed: dayjs('2025-02-13T11:44'),
};

export const sampleWithPartialData: IItem = {
  id: 8421,
  itemTitle: 'other buttery',
  itemPrice: 1062.23,
  itemSize: 'thorny finished ragged',
  itemCondition: 'WELLUSED',
  itemCategory: 'CLOTHING',
  description: '../fake-data/blob/hipster.txt',
  itemColour: 'into fearless',
  itemImage: '../fake-data/blob/hipster.png',
  itemImageContentType: 'unknown',
  timeListed: dayjs('2025-02-13T06:20'),
};

export const sampleWithFullData: IItem = {
  id: 32221,
  itemTitle: 'mixture stock',
  itemPrice: 12755.91,
  itemSize: 'pleasant',
  itemCondition: 'FAIRLYUSED',
  itemCategory: 'FOOTWEAR',
  description: '../fake-data/blob/hipster.txt',
  itemColour: 'brr',
  itemImage: '../fake-data/blob/hipster.png',
  itemImageContentType: 'unknown',
  timeListed: dayjs('2025-02-12T15:43'),
  itemLike: true,
};

export const sampleWithNewData: NewItem = {
  itemTitle: 'forenenst yippee limply',
  itemPrice: 15570.39,
  itemCondition: 'HEAVILYUSED',
  itemCategory: 'ELECTRONICS',
  itemImage: '../fake-data/blob/hipster.png',
  itemImageContentType: 'unknown',
  timeListed: dayjs('2025-02-13T10:55'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
