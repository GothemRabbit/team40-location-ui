import { IAuthentication, NewAuthentication } from './authentication.model';

export const sampleWithRequiredData: IAuthentication = {
  id: 2916,
  password: 'out excepting misspend',
};

export const sampleWithPartialData: IAuthentication = {
  id: 21058,
  password: 'than',
};

export const sampleWithFullData: IAuthentication = {
  id: 13707,
  password: 'reproachfully carouse even',
};

export const sampleWithNewData: NewAuthentication = {
  password: 'woot',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
