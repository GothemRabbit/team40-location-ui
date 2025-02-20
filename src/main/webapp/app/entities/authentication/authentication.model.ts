export interface IAuthentication {
  id: number;
  password?: string | null;
}

export type NewAuthentication = Omit<IAuthentication, 'id'> & { id: null };
