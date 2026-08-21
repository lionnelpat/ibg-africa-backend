import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IEtudiant, NewEtudiant } from '../etudiant.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEtudiant for edit and NewEtudiantFormGroupInput for create.
 */
type EtudiantFormGroupInput = IEtudiant | PartialWithRequiredKeyOf<NewEtudiant>;

type EtudiantFormDefaults = Pick<NewEtudiant, 'id' | 'cursusAcheve' | 'actif'>;

type EtudiantFormGroupContent = {
  id: FormControl<IEtudiant['id'] | NewEtudiant['id']>;
  matricule: FormControl<IEtudiant['matricule']>;
  nom: FormControl<IEtudiant['nom']>;
  prenom: FormControl<IEtudiant['prenom']>;
  particularite: FormControl<IEtudiant['particularite']>;
  dateNaissance: FormControl<IEtudiant['dateNaissance']>;
  email: FormControl<IEtudiant['email']>;
  telephone: FormControl<IEtudiant['telephone']>;
  anneeEntree: FormControl<IEtudiant['anneeEntree']>;
  cursusAcheve: FormControl<IEtudiant['cursusAcheve']>;
  anneeFinale: FormControl<IEtudiant['anneeFinale']>;
  keycloakUserId: FormControl<IEtudiant['keycloakUserId']>;
  commentaire: FormControl<IEtudiant['commentaire']>;
  actif: FormControl<IEtudiant['actif']>;
  pays: FormControl<IEtudiant['pays']>;
};

export type EtudiantFormGroup = FormGroup<EtudiantFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EtudiantFormService {
  createEtudiantFormGroup(etudiant: EtudiantFormGroupInput = { id: null }): EtudiantFormGroup {
    const etudiantRawValue = {
      ...this.getFormDefaults(),
      ...etudiant,
    };
    return new FormGroup<EtudiantFormGroupContent>({
      id: new FormControl(
        { value: etudiantRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      matricule: new FormControl(etudiantRawValue.matricule, {
        validators: [Validators.maxLength(30)],
      }),
      nom: new FormControl(etudiantRawValue.nom, {
        validators: [Validators.required, Validators.maxLength(80)],
      }),
      prenom: new FormControl(etudiantRawValue.prenom, {
        validators: [Validators.required, Validators.maxLength(80)],
      }),
      particularite: new FormControl(etudiantRawValue.particularite, {
        validators: [Validators.maxLength(80)],
      }),
      dateNaissance: new FormControl(etudiantRawValue.dateNaissance),
      email: new FormControl(etudiantRawValue.email, {
        validators: [Validators.maxLength(150)],
      }),
      telephone: new FormControl(etudiantRawValue.telephone, {
        validators: [Validators.maxLength(30)],
      }),
      anneeEntree: new FormControl(etudiantRawValue.anneeEntree, {
        validators: [Validators.min(1900), Validators.max(2200)],
      }),
      cursusAcheve: new FormControl(etudiantRawValue.cursusAcheve, {
        validators: [Validators.required],
      }),
      anneeFinale: new FormControl(etudiantRawValue.anneeFinale, {
        validators: [Validators.min(1900), Validators.max(2200)],
      }),
      keycloakUserId: new FormControl(etudiantRawValue.keycloakUserId, {
        validators: [Validators.maxLength(64)],
      }),
      commentaire: new FormControl(etudiantRawValue.commentaire, {
        validators: [Validators.maxLength(255)],
      }),
      actif: new FormControl(etudiantRawValue.actif, {
        validators: [Validators.required],
      }),
      pays: new FormControl(etudiantRawValue.pays),
    });
  }

  getEtudiant(form: EtudiantFormGroup): IEtudiant | NewEtudiant {
    return form.getRawValue() as IEtudiant | NewEtudiant;
  }

  resetForm(form: EtudiantFormGroup, etudiant: EtudiantFormGroupInput): void {
    const etudiantRawValue = { ...this.getFormDefaults(), ...etudiant };
    form.reset(
      {
        ...etudiantRawValue,
        id: { value: etudiantRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): EtudiantFormDefaults {
    return {
      id: null,
      cursusAcheve: false,
      actif: false,
    };
  }
}
