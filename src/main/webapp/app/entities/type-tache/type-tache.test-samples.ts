import { ITypeTache, NewTypeTache } from './type-tache.model';

export const sampleWithRequiredData: ITypeTache = {
  id: 8508,
  code: 'pour que abîmer étant donné qu',
  intitule: 'drelin blablabla ouah',
  entreDansMoyenne: false,
  actif: true,
};

export const sampleWithPartialData: ITypeTache = {
  id: 30323,
  code: 'hi',
  intitule: 'dehors atchoum énergique',
  libelleCourt: 'quoique de manière à ce que',
  entreDansMoyenne: false,
  actif: true,
};

export const sampleWithFullData: ITypeTache = {
  id: 16668,
  code: 'détendre',
  intitule: 'maintenant aïe',
  libelleLong: 'fourbe',
  libelleCourt: 'clientèle',
  entreDansMoyenne: false,
  commentaire: 'soupçonner ouch au moyen de',
  actif: false,
};

export const sampleWithNewData: NewTypeTache = {
  code: 'secouriste désormais électorat',
  intitule: 'quand puisque ficher',
  entreDansMoyenne: true,
  actif: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
