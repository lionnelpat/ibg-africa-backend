import { IParametre, NewParametre } from './parametre.model';

export const sampleWithRequiredData: IParametre = {
  id: 2886,
  cle: 'blême de crainte que hystérique',
  typeValeur: 'BOOLEEN',
  modifiableUi: true,
};

export const sampleWithPartialData: IParametre = {
  id: 19748,
  cle: 'minuscule snif mairie',
  libelle: 'tic-tac dès que composer',
  typeValeur: 'BOOLEEN',
  modifiableUi: true,
};

export const sampleWithFullData: IParametre = {
  id: 30867,
  cle: 'à travers outre',
  libelle: 'de crainte que si bien que sur',
  valeur: 'ménager',
  typeValeur: 'NOMBRE',
  modifiableUi: false,
};

export const sampleWithNewData: NewParametre = {
  cle: 'moyennant',
  typeValeur: 'NOMBRE',
  modifiableUi: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
