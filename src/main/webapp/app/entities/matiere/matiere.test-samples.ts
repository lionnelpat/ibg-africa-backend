import { IMatiere, NewMatiere } from './matiere.model';

export const sampleWithRequiredData: IMatiere = {
  id: 19021,
  intitule: 'quand de façon que',
  actif: true,
};

export const sampleWithPartialData: IMatiere = {
  id: 20225,
  intitule: 'concernant gens',
  libelleCourt: 'touchant dense chercher',
  actif: false,
};

export const sampleWithFullData: IMatiere = {
  id: 1103,
  intitule: 'pisser absolument corps enseignant',
  libelleLong: 'cuicui',
  libelleCourt: 'par séculaire',
  commentaire: 'gestionnaire ainsi',
  actif: true,
};

export const sampleWithNewData: NewMatiere = {
  intitule: 'dans la mesure où ébranler',
  actif: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
