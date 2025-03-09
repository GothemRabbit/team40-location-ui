import dayjs from 'dayjs/esm';

import { IItem, NewItem } from './item.model';

export const sampleWithRequiredData: IItem = {
  id: 23448,
  title: 'insidious gasp',
  price: 26914.01,
  condition: 'HEAVILYUSED',
  category: 'HOBBIES',
  timeListed: dayjs('2025-02-12T16:36'),
};

export const sampleWithPartialData: IItem = {
  id: 3313,
  title: 'zany rationalise',
  price: 9644.16,
  condition: 'BRANDNEW',
  category: 'BAGS',
  sizeItem: 'igloo emboss',
  timeListed: dayjs('2025-02-12T22:12'),
};

export const sampleWithFullData: IItem = {
  id: 19481,
  title: 'arrogantly',
  price: 30946.62,
  condition: 'BRANDNEW',
  category: 'HEADWEAR',
  description: '../fake-data/blob/hipster.txt',
  sizeItem: 'drat dream',
  brand: 'considering strictly',
  colour: 'proselytise near',
  timeListed: dayjs('2025-02-13T11:58'),
};

export const sampleWithNewData: NewItem = {
  title: 'disarm eulogise past',
  price: 10198.08,
  condition: 'HEAVILYUSED',
  category: 'HEADWEAR',
  timeListed: dayjs('2025-02-12T16:29'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
