import dayjs from 'dayjs/esm';

import { IEtudiant, NewEtudiant } from './etudiant.model';

export const sampleWithRequiredData: IEtudiant = {
  id: 29477,
  nom: 'à cause de',
  prenom: 'relire membre du personnel membre à vie',
  cursusAcheve: true,
  actif: false,
};

export const sampleWithPartialData: IEtudiant = {
  id: 31009,
  nom: "avare jamais à l'entour de",
  prenom: 'plic passer patientèle',
  particularite: 'vu que dynamique bien que',
  telephone: '+33 591241097',
  cursusAcheve: false,
  anneeFinale: 2080,
  actif: false,
};

export const sampleWithFullData: IEtudiant = {
  id: 938,
  matricule: 'entièrement que lâcher',
  nom: 'avant-hier',
  prenom: 'population du Québec responsable',
  particularite: 'distribuer de manière à ce que souple',
  dateNaissance: dayjs('2026-08-20'),
  email: 'Severin.Jean17@yahoo.fr',
  telephone: '+33 436093379',
  anneeEntree: 2037,
  cursusAcheve: true,
  anneeFinale: 2175,
  keycloakUserId: 'jadis',
  commentaire: 'sans que patientèle camper',
  actif: false,
};

export const sampleWithNewData: NewEtudiant = {
  nom: 'broum complètement camarade',
  prenom: 'assez près en faveur de',
  cursusAcheve: false,
  actif: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
