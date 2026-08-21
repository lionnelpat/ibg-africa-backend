import dayjs from 'dayjs/esm';

import { IEvaluationRealisee, NewEvaluationRealisee } from './evaluation-realisee.model';

export const sampleWithRequiredData: IEvaluationRealisee = {
  id: 23785,
  statut: 'VALIDEE',
  compteDansMoyenne: false,
};

export const sampleWithPartialData: IEvaluationRealisee = {
  id: 12584,
  note: 3192.83,
  statut: 'DISPENSE',
  compteDansMoyenne: false,
  dateDebut: dayjs('2026-08-20'),
  commentaire1: 'électorat',
  commentaire3: 'vers',
};

export const sampleWithFullData: IEvaluationRealisee = {
  id: 9558,
  note: 28068.96,
  statut: 'VALIDEE',
  compteDansMoyenne: true,
  dateDebut: dayjs('2026-08-20'),
  dateFin: dayjs('2026-08-20'),
  commentaire1: 'patientèle',
  commentaire2: 'conseil municipal drelin',
  commentaire3: 'adresser grâce à',
  saisiePar: 'près de',
  saisieLe: dayjs('2026-08-20T05:41'),
  valideePar: 'délectable patientèle',
  valideeLe: dayjs('2026-08-20T17:32'),
};

export const sampleWithNewData: NewEvaluationRealisee = {
  statut: 'VALIDEE',
  compteDansMoyenne: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
