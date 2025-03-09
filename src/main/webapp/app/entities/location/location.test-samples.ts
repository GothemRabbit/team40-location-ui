import { ILocation, NewLocation } from './location.model';

export const sampleWithRequiredData: ILocation = {
  id: 30532,
  address: 'warm',
  latitude: 19141.56,
  longitude: 21240.05,
  postcode: 'since popularity',
};

export const sampleWithPartialData: ILocation = {
  id: 5225,
  address: 'for transcend',
  latitude: 27333.49,
  longitude: 20773.59,
  postcode: 'until bleak shoulder',
};

export const sampleWithFullData: ILocation = {
  id: 13099,
  address: 'mob',
  latitude: 30495.66,
  longitude: 23160.16,
  postcode: 'tensely drat ignorance',
};

export const sampleWithNewData: NewLocation = {
  address: 'decryption enlightened ouch',
  latitude: 29777.9,
  longitude: 4480.9,
  postcode: 'spotless',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
