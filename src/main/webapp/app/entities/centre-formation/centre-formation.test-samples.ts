import { ICentreFormation, NewCentreFormation } from './centre-formation.model';

export const sampleWithRequiredData: ICentreFormation = {
  id: 19334,
  code: 'doucement à partir d',
  nom: 'certainement de sorte que',
  ville: 'par suite de',
  signataire: 'sauvage tant que',
  nbCyclesCursus: 13,
  noteMaximale: 9783.41,
  actif: false,
};

export const sampleWithPartialData: ICentreFormation = {
  id: 22880,
  code: 'soudain déceler',
  nom: 'partenaire guide',
  ville: 'célébrer lunatique',
  signataire: 'ding de façon que communauté étudiante',
  logoUrl: 'avant que blême',
  nbCyclesCursus: 4,
  noteMaximale: 28135.11,
  actif: true,
};

export const sampleWithFullData: ICentreFormation = {
  id: 12573,
  code: 'selon à condition qu',
  nom: 'ainsi',
  ville: 'si',
  adresse: 'triangulaire',
  enteteDocument: '../fake-data/blob/hipster.txt',
  signataire: 'impromptu oui dorénavant',
  logoUrl: 'main-d’œuvre diplomate',
  nbCyclesCursus: 14,
  noteMaximale: 23496.32,
  actif: false,
};

export const sampleWithNewData: NewCentreFormation = {
  code: 'affable secours',
  nom: 'grrr revenir afin que',
  ville: 'quant à jusqu’à ce que direction',
  signataire: 'avant tout à fait brave',
  nbCyclesCursus: 16,
  noteMaximale: 12208.57,
  actif: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
