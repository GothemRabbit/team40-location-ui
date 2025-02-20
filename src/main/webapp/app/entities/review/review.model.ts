import dayjs from 'dayjs/esm';

export interface IReview {
  id: number;
  rating?: number | null;
  reviewText?: string | null;
  reviewDate?: dayjs.Dayjs | null;
}

export type NewReview = Omit<IReview, 'id'> & { id: null };
