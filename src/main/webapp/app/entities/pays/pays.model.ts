export interface IPays {
  id: number;
  codeIso?: string | null;
  nom?: string | null;
  langue?: string | null;
  fuseau?: string | null;
  actif?: boolean | null;
}

export type NewPays = Omit<IPays, 'id'> & { id: null };
