import dayjs from 'dayjs/esm';

import { IHistoriqueNote } from './historique-note.model';

export const sampleWithRequiredData: IHistoriqueNote = {
  id: 3088,
  modifiePar: 'équipe de recherche que croâ',
  modifieLe: dayjs('2026-08-20T12:58'),
};

export const sampleWithPartialData: IHistoriqueNote = {
  id: 29777,
  noteApres: 27135.82,
  statutApres: 'NON_SAISIE',
  motif: 'alors que',
  modifiePar: 'communauté étudiante outre de peur que',
  modifieLe: dayjs('2026-08-20T18:07'),
};

export const sampleWithFullData: IHistoriqueNote = {
  id: 29298,
  noteAvant: 3028.07,
  noteApres: 28314.58,
  statutAvant: 'DISPENSE',
  statutApres: 'NON_SAISIE',
  motif: 'douter tant',
  modifiePar: 'multiple presque aux alentours de',
  modifieLe: dayjs('2026-08-20T14:10'),
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
