import { IWishlist, NewWishlist } from './wishlist.model';

export const sampleWithRequiredData: IWishlist = {
  id: 32300,
  name: 'meh ownership ready',
  visibility: 'PRIVATE',
};

export const sampleWithPartialData: IWishlist = {
  id: 10878,
  name: 'within earnest fairly',
  visibility: 'PUBLIC',
};

export const sampleWithFullData: IWishlist = {
  id: 10808,
  name: 'molasses a',
  visibility: 'PRIVATE',
};

export const sampleWithNewData: NewWishlist = {
  name: 'midst pish',
  visibility: 'PUBLIC',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
