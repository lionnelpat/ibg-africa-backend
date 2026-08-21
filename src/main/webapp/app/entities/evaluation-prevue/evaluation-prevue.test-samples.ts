import dayjs from 'dayjs/esm';

import { IEvaluationPrevue, NewEvaluationPrevue } from './evaluation-prevue.model';

export const sampleWithRequiredData: IEvaluationPrevue = {
  id: 10592,
  intitule: 'enseigner',
  libelleImpression: 'combien',
  coefficient: 21279.44,
  compteDansMoyenne: true,
  noteMaximale: 6937.99,
};

export const sampleWithPartialData: IEvaluationPrevue = {
  id: 25194,
  intitule: 'cuicui',
  libelleImpression: 'vraiment placide',
  coefficient: 25259.89,
  compteDansMoyenne: false,
  noteMaximale: 8250.94,
  dateFin: dayjs('2026-08-20'),
};

export const sampleWithFullData: IEvaluationPrevue = {
  id: 14684,
  intitule: 'vorace loin',
  libelleImpression: 'gestionnaire jadis',
  coefficient: 15862.59,
  compteDansMoyenne: false,
  noteMaximale: 3961.13,
  dateDebut: dayjs('2026-08-20'),
  dateFin: dayjs('2026-08-20'),
  commentaire: 'a splendide',
};

export const sampleWithNewData: NewEvaluationPrevue = {
  intitule: 'assigner au défaut de',
  libelleImpression: 'expliquer au point que avare',
  coefficient: 14383.58,
  compteDansMoyenne: true,
  noteMaximale: 19755.74,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
