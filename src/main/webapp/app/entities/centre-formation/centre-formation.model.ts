import { IPays } from 'app/entities/pays/pays.model';

export interface ICentreFormation {
  id: number;
  code?: string | null;
  nom?: string | null;
  ville?: string | null;
  adresse?: string | null;
  enteteDocument?: string | null;
  signataire?: string | null;
  logoUrl?: string | null;
  nbCyclesCursus?: number | null;
  noteMaximale?: number | null;
  actif?: boolean | null;
  pays?: Pick<IPays, 'id' | 'nom'> | null;
}

export type NewCentreFormation = Omit<ICentreFormation, 'id'> & { id: null };
