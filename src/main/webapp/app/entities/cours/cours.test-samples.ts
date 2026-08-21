import dayjs from 'dayjs/esm';

import { ICours, NewCours } from './cours.model';

export const sampleWithRequiredData: ICours = {
  id: 4410,
  intitule: 'bof',
  ordreAffichage: 9219,
  coefficient: 25618.05,
  actif: false,
};

export const sampleWithPartialData: ICours = {
  id: 20044,
  intitule: 'toc-toc',
  ordreAffichage: 1802,
  coefficient: 5530.3,
  dateDebut: dayjs('2026-08-20'),
  dateFin: dayjs('2026-08-20'),
  actif: true,
};

export const sampleWithFullData: ICours = {
  id: 27337,
  intitule: 'placide psitt',
  libelleLong: 'clac',
  libelleCourt: 'rectorat multiple commis de cuisine',
  ordreAffichage: 11919,
  nbPeriodes: 7404,
  coefficient: 19668.4,
  dateDebut: dayjs('2026-08-20'),
  dateFin: dayjs('2026-08-20'),
  commentaire: 'groin groin chialer habile',
  actif: true,
};

export const sampleWithNewData: NewCours = {
  intitule: 'meuh',
  ordreAffichage: 11828,
  coefficient: 28529.16,
  actif: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
