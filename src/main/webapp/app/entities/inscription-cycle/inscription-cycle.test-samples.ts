import dayjs from 'dayjs/esm';

import { IInscriptionCycle, NewInscriptionCycle } from './inscription-cycle.model';

export const sampleWithRequiredData: IInscriptionCycle = {
  id: 18358,
  cycleTermine: true,
};

export const sampleWithPartialData: IInscriptionCycle = {
  id: 27600,
  cycleTermine: false,
  commentaire1: 'd’autant que direction tsoin-tsoin',
  commentaire3: 'ça ouf hi',
  commentaire5: 'fade transporter',
};

export const sampleWithFullData: IInscriptionCycle = {
  id: 6201,
  dateInscription: dayjs('2026-08-20'),
  cycleTermine: false,
  groupe: 'correspondre tchou tchouu',
  commentaire1: 'pendant que',
  commentaire2: 'étant donné que',
  commentaire3: 'aux environs de tellement sans que',
  commentaire5: 'tandis que sur',
};

export const sampleWithNewData: NewInscriptionCycle = {
  cycleTermine: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
