import { ICentreFormation } from 'app/entities/centre-formation/centre-formation.model';

export interface IBaremeMention {
  id: number;
  libelleLong?: string | null;
  libelleCourt?: string | null;
  borneMin?: number | null;
  minInclus?: boolean | null;
  borneMax?: number | null;
  maxInclus?: boolean | null;
  ordreAffichage?: number | null;
  commentaire?: string | null;
  actif?: boolean | null;
  centre?: Pick<ICentreFormation, 'id' | 'code'> | null;
}

export type NewBaremeMention = Omit<IBaremeMention, 'id'> & { id: null };
