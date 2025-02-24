import { IAuthentication, NewAuthentication } from './authentication.model';

export const sampleWithRequiredData: IAuthentication = {
  id: 989,
  password: 'mixed',
};

export const sampleWithPartialData: IAuthentication = {
  id: 27765,
  password: 'colorfully',
};

export const sampleWithFullData: IAuthentication = {
  id: 22477,
  password: 'leading swing',
};

export const sampleWithNewData: NewAuthentication = {
  password: 'now',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
