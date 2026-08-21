import dayjs from 'dayjs/esm';
import { IEvaluationRealisee } from 'app/entities/evaluation-realisee/evaluation-realisee.model';
import { StatutNote } from 'app/entities/enumerations/statut-note.model';

export interface IHistoriqueNote {
  id: number;
  noteAvant?: number | null;
  noteApres?: number | null;
  statutAvant?: keyof typeof StatutNote | null;
  statutApres?: keyof typeof StatutNote | null;
  motif?: string | null;
  modifiePar?: string | null;
  modifieLe?: dayjs.Dayjs | null;
  evaluationRealisee?: Pick<IEvaluationRealisee, 'id' | 'statut'> | null;
}
