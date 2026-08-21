export interface IEnseignant {
  id: number;
  nom?: string | null;
  prenom?: string | null;
  libelleLong?: string | null;
  libelleCourt?: string | null;
  email?: string | null;
  telephone?: string | null;
  keycloakUserId?: string | null;
  commentaire?: string | null;
  actif?: boolean | null;
}

export type NewEnseignant = Omit<IEnseignant, 'id'> & { id: null };
