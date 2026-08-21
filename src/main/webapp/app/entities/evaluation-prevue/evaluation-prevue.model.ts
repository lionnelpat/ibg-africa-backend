import dayjs from 'dayjs/esm';
import { ICycle } from 'app/entities/cycle/cycle.model';
import { IEnseignant } from 'app/entities/enseignant/enseignant.model';
import { IMatiere } from 'app/entities/matiere/matiere.model';
import { ISousMatiere } from 'app/entities/sous-matiere/sous-matiere.model';
import { ICours } from 'app/entities/cours/cours.model';
import { ITypeTache } from 'app/entities/type-tache/type-tache.model';

export interface IEvaluationPrevue {
  id: number;
  intitule?: string | null;
  libelleImpression?: string | null;
  coefficient?: number | null;
  compteDansMoyenne?: boolean | null;
  noteMaximale?: number | null;
  dateDebut?: dayjs.Dayjs | null;
  dateFin?: dayjs.Dayjs | null;
  commentaire?: string | null;
  cycle?: Pick<ICycle, 'id' | 'annee'> | null;
  enseignant?: Pick<IEnseignant, 'id' | 'nom'> | null;
  matiere?: Pick<IMatiere, 'id' | 'intitule'> | null;
  sousMatiere?: Pick<ISousMatiere, 'id' | 'intitule'> | null;
  cours?: Pick<ICours, 'id' | 'intitule'> | null;
  typeTache?: Pick<ITypeTache, 'id' | 'intitule'> | null;
}

export type NewEvaluationPrevue = Omit<IEvaluationPrevue, 'id'> & { id: null };
