import dayjs from 'dayjs/esm';
import { ICycle } from 'app/entities/cycle/cycle.model';
import { IEtudiant } from 'app/entities/etudiant/etudiant.model';

export interface IInscriptionCycle {
  id: number;
  dateInscription?: dayjs.Dayjs | null;
  cycleTermine?: boolean | null;
  groupe?: string | null;
  commentaire1?: string | null;
  commentaire2?: string | null;
  commentaire3?: string | null;
  commentaire5?: string | null;
  cycle?: Pick<ICycle, 'id' | 'annee'> | null;
  etudiant?: Pick<IEtudiant, 'id' | 'nom'> | null;
}

export type NewInscriptionCycle = Omit<IInscriptionCycle, 'id'> & { id: null };
