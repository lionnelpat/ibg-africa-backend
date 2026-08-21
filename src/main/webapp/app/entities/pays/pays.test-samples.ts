import { IPays, NewPays } from './pays.model';

export const sampleWithRequiredData: IPays = {
  id: 22948,
  codeIso: 'pr',
  nom: 'hier sur',
  langue: 'parle',
  actif: true,
};

export const sampleWithPartialData: IPays = {
  id: 23517,
  codeIso: 'en',
  nom: 'raser tellement au défaut de',
  langue: "d'apr",
  actif: false,
};

export const sampleWithFullData: IPays = {
  id: 19665,
  codeIso: 'a ',
  nom: 'environ croâ',
  langue: 'consi',
  fuseau: 'aïe dessous',
  actif: true,
};

export const sampleWithNewData: NewPays = {
  codeIso: 'pr',
  nom: 'enlever lors',
  langue: 'meuh',
  actif: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
