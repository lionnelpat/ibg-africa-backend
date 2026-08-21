import dayjs from 'dayjs/esm';

import { IHabilitationCycle, NewHabilitationCycle } from './habilitation-cycle.model';

export const sampleWithRequiredData: IHabilitationCycle = {
  id: 3683,
  keycloakUserId: 'candide hors de',
  roleFonctionnel: 'SCOLARITE',
};

export const sampleWithPartialData: IHabilitationCycle = {
  id: 2205,
  keycloakUserId: 'hier',
  roleFonctionnel: 'LECTEUR',
};

export const sampleWithFullData: IHabilitationCycle = {
  id: 15917,
  keycloakUserId: 'ça après que',
  roleFonctionnel: 'SCOLARITE',
  dateDebut: dayjs('2026-08-20'),
  dateFin: dayjs('2026-08-20'),
};

export const sampleWithNewData: NewHabilitationCycle = {
  keycloakUserId: 'simple',
  roleFonctionnel: 'SCOLARITE',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
