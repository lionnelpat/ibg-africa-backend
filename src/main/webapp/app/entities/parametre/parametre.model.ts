import { ICentreFormation } from 'app/entities/centre-formation/centre-formation.model';
import { TypeValeur } from 'app/entities/enumerations/type-valeur.model';

export interface IParametre {
  id: number;
  cle?: string | null;
  libelle?: string | null;
  valeur?: string | null;
  typeValeur?: keyof typeof TypeValeur | null;
  modifiableUi?: boolean | null;
  centre?: ICentreFormation | null;
}

export type NewParametre = Omit<IParametre, 'id'> & { id: null };
