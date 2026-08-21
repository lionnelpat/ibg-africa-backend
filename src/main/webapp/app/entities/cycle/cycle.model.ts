import dayjs from 'dayjs/esm';
import { ICentreFormation } from 'app/entities/centre-formation/centre-formation.model';

export interface ICycle {
  id: number;
  annee?: number | null;
  libelle?: string | null;
  dateDebut?: dayjs.Dayjs | null;
  dateFin?: dayjs.Dayjs | null;
  cloture?: boolean | null;
  commentaire?: string | null;
  centre?: Pick<ICentreFormation, 'id' | 'code'> | null;
}

export type NewCycle = Omit<ICycle, 'id'> & { id: null };
