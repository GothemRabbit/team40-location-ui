import { IAuthentication, NewAuthentication } from './authentication.model';

export const sampleWithRequiredData: IAuthentication = {
  id: 6971,
  password: 'impressionable',
};

export const sampleWithPartialData: IAuthentication = {
  id: 12368,
  password: 'bungalow um',
};

export const sampleWithFullData: IAuthentication = {
  id: 28235,
  password: 'tribe yowza',
};

export const sampleWithNewData: NewAuthentication = {
  password: 'oxygenate object hm',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
