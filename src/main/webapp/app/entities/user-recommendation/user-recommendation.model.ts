export interface IUserRecommendation {
  id: number;
  reason?: string | null;
}

export type NewUserRecommendation = Omit<IUserRecommendation, 'id'> & { id: null };
