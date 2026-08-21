export interface ITypeTache {
  id: number;
  code?: string | null;
  intitule?: string | null;
  libelleLong?: string | null;
  libelleCourt?: string | null;
  entreDansMoyenne?: boolean | null;
  commentaire?: string | null;
  actif?: boolean | null;
}

export type NewTypeTache = Omit<ITypeTache, 'id'> & { id: null };
