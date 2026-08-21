export interface IMatiere {
  id: number;
  intitule?: string | null;
  libelleLong?: string | null;
  libelleCourt?: string | null;
  commentaire?: string | null;
  actif?: boolean | null;
}

export type NewMatiere = Omit<IMatiere, 'id'> & { id: null };
