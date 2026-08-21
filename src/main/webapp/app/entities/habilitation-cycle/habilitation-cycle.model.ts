import dayjs from 'dayjs/esm';
import { ICentreFormation } from 'app/entities/centre-formation/centre-formation.model';
import { ICycle } from 'app/entities/cycle/cycle.model';
import { RoleFonctionnel } from 'app/entities/enumerations/role-fonctionnel.model';

export interface IHabilitationCycle {
  id: number;
  keycloakUserId?: string | null;
  roleFonctionnel?: keyof typeof RoleFonctionnel | null;
  dateDebut?: dayjs.Dayjs | null;
  dateFin?: dayjs.Dayjs | null;
  centre?: ICentreFormation | null;
  cycle?: ICycle | null;
}

export type NewHabilitationCycle = Omit<IHabilitationCycle, 'id'> & { id: null };
