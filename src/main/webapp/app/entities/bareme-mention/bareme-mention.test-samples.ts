import { IBaremeMention, NewBaremeMention } from './bareme-mention.model';

export const sampleWithRequiredData: IBaremeMention = {
  id: 8859,
  libelleLong: 'accueillir absolument pendant',
  libelleCourt: 'ouille mince',
  minInclus: false,
  maxInclus: false,
  ordreAffichage: 16393,
  actif: true,
};

export const sampleWithPartialData: IBaremeMention = {
  id: 20938,
  libelleLong: 'associer oh nouer',
  libelleCourt: 'crac incalculable',
  borneMin: 7321.61,
  minInclus: true,
  maxInclus: false,
  ordreAffichage: 6804,
  commentaire: 'émérite',
  actif: false,
};

export const sampleWithFullData: IBaremeMention = {
  id: 6553,
  libelleLong: 'meuh toc blablabla',
  libelleCourt: 'au-dehors plus vivace',
  borneMin: 23439.83,
  minInclus: true,
  borneMax: 26937.65,
  maxInclus: true,
  ordreAffichage: 31215,
  commentaire: 'glouglou presque',
  actif: false,
};

export const sampleWithNewData: NewBaremeMention = {
  libelleLong: 'pardonner assassiner',
  libelleCourt: 'bientôt tsoin-tsoin fidèle',
  minInclus: false,
  maxInclus: false,
  ordreAffichage: 18351,
  actif: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
