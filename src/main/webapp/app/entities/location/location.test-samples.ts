import { ILocation, NewLocation } from './location.model';

export const sampleWithRequiredData: ILocation = {
  id: 6633,
  address: 'er',
  latitude: 5979.82,
  longitude: 11862.34,
  postcode: 'whenever tennis',
};

export const sampleWithPartialData: ILocation = {
  id: 3257,
  address: 'lustrous going',
  latitude: 10497.67,
  longitude: 31931.64,
  postcode: 'cavernous oof',
};

export const sampleWithFullData: ILocation = {
  id: 6450,
  address: 'athwart dim by',
  latitude: 10243.75,
  longitude: 26783.47,
  postcode: 'searchingly exempt',
};

export const sampleWithNewData: NewLocation = {
  address: 'healthily',
  latitude: 14878.37,
  longitude: 8720.38,
  postcode: 'favorite',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
