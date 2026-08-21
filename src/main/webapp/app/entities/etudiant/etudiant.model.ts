import dayjs from 'dayjs/esm';
import { IPays } from 'app/entities/pays/pays.model';

export interface IEtudiant {
  id: number;
  matricule?: string | null;
  nom?: string | null;
  prenom?: string | null;
  particularite?: string | null;
  dateNaissance?: dayjs.Dayjs | null;
  email?: string | null;
  telephone?: string | null;
  anneeEntree?: number | null;
  cursusAcheve?: boolean | null;
  anneeFinale?: number | null;
  keycloakUserId?: string | null;
  commentaire?: string | null;
  actif?: boolean | null;
  pays?: Pick<IPays, 'id' | 'nom'> | null;
}

export type NewEtudiant = Omit<IEtudiant, 'id'> & { id: null };
