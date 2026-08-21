import { IEnseignant, NewEnseignant } from './enseignant.model';

export const sampleWithRequiredData: IEnseignant = {
  id: 14621,
  nom: 'novice',
  prenom: 'triathlète différencier de par',
  actif: true,
};

export const sampleWithPartialData: IEnseignant = {
  id: 22107,
  nom: "trier à l'exception de soulager",
  prenom: 'mélancolique derechef',
  libelleCourt: 'badaboum',
  email: 'Evelyne_Blanchard@hotmail.fr',
  telephone: '+33 178101202',
  keycloakUserId: 'snob de façon que sédentaire',
  commentaire: 'apparemment',
  actif: false,
};

export const sampleWithFullData: IEnseignant = {
  id: 22784,
  nom: 'délégation',
  prenom: 'via rudement',
  libelleLong: 'depuis sans que vétuste',
  libelleCourt: 'délégation dense',
  email: 'Lorrain.Clement@gmail.com',
  telephone: '+33 235468413',
  keycloakUserId: 'miaou concurrence',
  commentaire: 'commis',
  actif: false,
};

export const sampleWithNewData: NewEnseignant = {
  nom: 'errer',
  prenom: 'ouah y sans que',
  actif: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
