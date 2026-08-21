import dayjs from 'dayjs/esm';

import { IEvenementEtudiant, NewEvenementEtudiant } from './evenement-etudiant.model';

export const sampleWithRequiredData: IEvenementEtudiant = {
  id: 26396,
  intitule: 'apparemment toujours comme',
};

export const sampleWithPartialData: IEvenementEtudiant = {
  id: 13376,
  intitule: 'glouglou',
  commentaire: 'étant donné que quand choisir',
};

export const sampleWithFullData: IEvenementEtudiant = {
  id: 9525,
  dateEvenement: dayjs('2026-08-20'),
  intitule: 'secouer',
  commentaire: 'triathlète',
};

export const sampleWithNewData: NewEvenementEtudiant = {
  intitule: 'adorable près',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
