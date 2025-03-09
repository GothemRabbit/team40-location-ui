import dayjs from 'dayjs/esm';

import { IItem, NewItem } from './item.model';

export const sampleWithRequiredData: IItem = {
  id: 21196,
  title: 'key',
  price: 7453.16,
  condition: 'FAIRLYUSED',
  category: 'FOOTWEAR',
  timeListed: dayjs('2025-02-12T21:42'),
};

export const sampleWithPartialData: IItem = {
  id: 502,
  title: 'certainly conjecture whoever',
  price: 13157.48,
  condition: 'FAIRLYUSED',
  category: 'HEADWEAR',
  description: '../fake-data/blob/hipster.txt',
  timeListed: dayjs('2025-02-13T00:18'),
};

export const sampleWithFullData: IItem = {
  id: 821,
  title: 'quicker',
  price: 6721.6,
  condition: 'LIKENEW',
  category: 'HOBBIES',
  description: '../fake-data/blob/hipster.txt',
  brand: 'loftily toward republican',
  colour: 'freckle where',
  timeListed: dayjs('2025-02-13T05:56'),
};

export const sampleWithNewData: NewItem = {
  title: 'midst',
  price: 12772.57,
  condition: 'LIKENEW',
  category: 'ELECTRONICS',
  timeListed: dayjs('2025-02-12T23:51'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
