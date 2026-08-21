import { ISousMatiere, NewSousMatiere } from './sous-matiere.model';

export const sampleWithRequiredData: ISousMatiere = {
  id: 29865,
  intitule: 'fade mentionner',
  actif: false,
};

export const sampleWithPartialData: ISousMatiere = {
  id: 17707,
  intitule: 'gratis',
  libelleLong: 'à travers croâ fonctionnaire',
  libelleCourt: 'd’autant que clientèle vlan',
  actif: false,
};

export const sampleWithFullData: ISousMatiere = {
  id: 7804,
  intitule: 'adresser assez suivant',
  libelleLong: 'de façon que régner',
  libelleCourt: 'de façon à chef de cuisine',
  commentaire: 'en decà de turquoise vis-à-vie de',
  actif: true,
};

export const sampleWithNewData: NewSousMatiere = {
  intitule: 'combien commis',
  actif: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
