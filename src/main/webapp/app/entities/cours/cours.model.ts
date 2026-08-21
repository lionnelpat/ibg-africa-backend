import dayjs from 'dayjs/esm';

export interface ICours {
  id: number;
  intitule?: string | null;
  libelleLong?: string | null;
  libelleCourt?: string | null;
  ordreAffichage?: number | null;
  nbPeriodes?: number | null;
  coefficient?: number | null;
  dateDebut?: dayjs.Dayjs | null;
  dateFin?: dayjs.Dayjs | null;
  commentaire?: string | null;
  actif?: boolean | null;
}

export type NewCours = Omit<ICours, 'id'> & { id: null };
