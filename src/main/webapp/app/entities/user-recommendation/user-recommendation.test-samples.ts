import { IUserRecommendation, NewUserRecommendation } from './user-recommendation.model';

export const sampleWithRequiredData: IUserRecommendation = {
  id: 26706,
};

export const sampleWithPartialData: IUserRecommendation = {
  id: 10482,
};

export const sampleWithFullData: IUserRecommendation = {
  id: 6411,
  reason: 'probate unethically hotfoot',
};

export const sampleWithNewData: NewUserRecommendation = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
