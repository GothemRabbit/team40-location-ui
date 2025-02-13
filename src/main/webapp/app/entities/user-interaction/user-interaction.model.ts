import dayjs from 'dayjs/esm';
import { InteractionType } from 'app/entities/enumerations/interaction-type.model';

export interface IUserInteraction {
  id: number;
  type?: keyof typeof InteractionType | null;
  details?: string | null;
  interactionDate?: dayjs.Dayjs | null;
}

export type NewUserInteraction = Omit<IUserInteraction, 'id'> & { id: null };
