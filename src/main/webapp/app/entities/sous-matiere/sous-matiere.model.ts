export interface ISousMatiere {
  id: number;
  intitule?: string | null;
  libelleLong?: string | null;
  libelleCourt?: string | null;
  commentaire?: string | null;
  actif?: boolean | null;
}

export type NewSousMatiere = Omit<ISousMatiere, 'id'> & { id: null };
