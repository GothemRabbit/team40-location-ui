import dayjs from 'dayjs/esm';

import { IItem, NewItem } from './item.model';

export const sampleWithRequiredData: IItem = {
  id: 12454,
  title: 'official sweetly blah',
  price: 20688.13,
  condition: 'HEAVILYUSED',
  category: 'FOOTWEAR',
  timeListed: dayjs('2025-02-12T22:47'),
};

export const sampleWithPartialData: IItem = {
  id: 19182,
  title: 'for yuck accept',
  price: 7106.62,
  condition: 'LIKENEW',
  category: 'OTHERS',
  timeListed: dayjs('2025-02-13T06:08'),
};

export const sampleWithFullData: IItem = {
  id: 23218,
  title: 'ofX',
  price: 30114.78,
  condition: 'BRANDNEW',
  category: 'FOOTWEAR',
  description: '../fake-data/blob/hipster.txt',
  brand: 'finally fail who',
  colour: 'artistic outrun',
  timeListed: dayjs('2025-02-13T06:04'),
};

export const sampleWithNewData: NewItem = {
  title: 'abaft',
  price: 3849.85,
  condition: 'FAIRLYUSED',
  category: 'OTHERS',
  timeListed: dayjs('2025-02-13T07:43'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
