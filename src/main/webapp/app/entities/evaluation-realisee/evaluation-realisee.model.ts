import dayjs from 'dayjs/esm';
import { IEvaluationPrevue } from 'app/entities/evaluation-prevue/evaluation-prevue.model';
import { IEtudiant } from 'app/entities/etudiant/etudiant.model';
import { StatutNote } from 'app/entities/enumerations/statut-note.model';

export interface IEvaluationRealisee {
  id: number;
  note?: number | null;
  statut?: keyof typeof StatutNote | null;
  compteDansMoyenne?: boolean | null;
  dateDebut?: dayjs.Dayjs | null;
  dateFin?: dayjs.Dayjs | null;
  commentaire1?: string | null;
  commentaire2?: string | null;
  commentaire3?: string | null;
  saisiePar?: string | null;
  saisieLe?: dayjs.Dayjs | null;
  valideePar?: string | null;
  valideeLe?: dayjs.Dayjs | null;
  evaluationPrevue?: Pick<IEvaluationPrevue, 'id' | 'intitule'> | null;
  etudiant?: Pick<IEtudiant, 'id' | 'nom'> | null;
}

export type NewEvaluationRealisee = Omit<IEvaluationRealisee, 'id'> & { id: null };
