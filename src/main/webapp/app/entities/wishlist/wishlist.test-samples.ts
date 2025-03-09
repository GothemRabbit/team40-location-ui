import { IWishlist, NewWishlist } from './wishlist.model';

export const sampleWithRequiredData: IWishlist = {
  id: 996,
  name: 'blah since',
  visibility: 'PUBLIC',
};

export const sampleWithPartialData: IWishlist = {
  id: 28030,
  name: 'roadway neglected questioningly',
  visibility: 'PRIVATE',
};

export const sampleWithFullData: IWishlist = {
  id: 6639,
  name: 'notwithstanding sham',
  visibility: 'PUBLIC',
};

export const sampleWithNewData: NewWishlist = {
  name: 'whose',
  visibility: 'PUBLIC',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
