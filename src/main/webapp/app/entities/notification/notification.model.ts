import dayjs from 'dayjs/esm';
import { NotificationType } from 'app/entities/enumerations/notification-type.model';

export interface INotification {
  id: number;
  title?: string | null;
  message?: string | null;
  notificationType?: keyof typeof NotificationType | null;
  isRead?: boolean | null;
  createdAt?: dayjs.Dayjs | null;
}

export type NewNotification = Omit<INotification, 'id'> & { id: null };
