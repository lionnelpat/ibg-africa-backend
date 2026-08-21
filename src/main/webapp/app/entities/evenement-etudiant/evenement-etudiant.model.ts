import dayjs from 'dayjs/esm';
import { IEtudiant } from 'app/entities/etudiant/etudiant.model';

export interface IEvenementEtudiant {
  id: number;
  dateEvenement?: dayjs.Dayjs | null;
  intitule?: string | null;
  commentaire?: string | null;
  etudiant?: IEtudiant | null;
}

export type NewEvenementEtudiant = Omit<IEvenementEtudiant, 'id'> & { id: null };
