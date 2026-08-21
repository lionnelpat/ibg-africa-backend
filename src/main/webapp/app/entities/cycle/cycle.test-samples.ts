import dayjs from 'dayjs/esm';

import { ICycle, NewCycle } from './cycle.model';

export const sampleWithRequiredData: ICycle = {
  id: 26488,
  annee: 2192,
  cloture: false,
};

export const sampleWithPartialData: ICycle = {
  id: 7691,
  annee: 1907,
  libelle: 'vlan conseil d’administration dessus',
  dateDebut: dayjs('2026-08-20'),
  dateFin: dayjs('2026-08-20'),
  cloture: true,
};

export const sampleWithFullData: ICycle = {
  id: 5633,
  annee: 2193,
  libelle: 'personnel professionnel tard',
  dateDebut: dayjs('2026-08-20'),
  dateFin: dayjs('2026-08-20'),
  cloture: true,
  commentaire: 'grrr oh',
};

export const sampleWithNewData: NewCycle = {
  annee: 1900,
  cloture: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
