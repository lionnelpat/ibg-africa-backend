import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IEnseignant, NewEnseignant } from '../enseignant.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEnseignant for edit and NewEnseignantFormGroupInput for create.
 */
type EnseignantFormGroupInput = IEnseignant | PartialWithRequiredKeyOf<NewEnseignant>;

type EnseignantFormDefaults = Pick<NewEnseignant, 'id' | 'actif'>;

type EnseignantFormGroupContent = {
  id: FormControl<IEnseignant['id'] | NewEnseignant['id']>;
  nom: FormControl<IEnseignant['nom']>;
  prenom: FormControl<IEnseignant['prenom']>;
  libelleLong: FormControl<IEnseignant['libelleLong']>;
  libelleCourt: FormControl<IEnseignant['libelleCourt']>;
  email: FormControl<IEnseignant['email']>;
  telephone: FormControl<IEnseignant['telephone']>;
  keycloakUserId: FormControl<IEnseignant['keycloakUserId']>;
  commentaire: FormControl<IEnseignant['commentaire']>;
  actif: FormControl<IEnseignant['actif']>;
};

export type EnseignantFormGroup = FormGroup<EnseignantFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EnseignantFormService {
  createEnseignantFormGroup(enseignant: EnseignantFormGroupInput = { id: null }): EnseignantFormGroup {
    const enseignantRawValue = {
      ...this.getFormDefaults(),
      ...enseignant,
    };
    return new FormGroup<EnseignantFormGroupContent>({
      id: new FormControl(
        { value: enseignantRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nom: new FormControl(enseignantRawValue.nom, {
        validators: [Validators.required, Validators.maxLength(80)],
      }),
      prenom: new FormControl(enseignantRawValue.prenom, {
        validators: [Validators.required, Validators.maxLength(80)],
      }),
      libelleLong: new FormControl(enseignantRawValue.libelleLong, {
        validators: [Validators.maxLength(100)],
      }),
      libelleCourt: new FormControl(enseignantRawValue.libelleCourt, {
        validators: [Validators.maxLength(50)],
      }),
      email: new FormControl(enseignantRawValue.email, {
        validators: [Validators.maxLength(150)],
      }),
      telephone: new FormControl(enseignantRawValue.telephone, {
        validators: [Validators.maxLength(30)],
      }),
      keycloakUserId: new FormControl(enseignantRawValue.keycloakUserId, {
        validators: [Validators.maxLength(64)],
      }),
      commentaire: new FormControl(enseignantRawValue.commentaire, {
        validators: [Validators.maxLength(255)],
      }),
      actif: new FormControl(enseignantRawValue.actif, {
        validators: [Validators.required],
      }),
    });
  }

  getEnseignant(form: EnseignantFormGroup): IEnseignant | NewEnseignant {
    return form.getRawValue() as IEnseignant | NewEnseignant;
  }

  resetForm(form: EnseignantFormGroup, enseignant: EnseignantFormGroupInput): void {
    const enseignantRawValue = { ...this.getFormDefaults(), ...enseignant };
    form.reset(
      {
        ...enseignantRawValue,
        id: { value: enseignantRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): EnseignantFormDefaults {
    return {
      id: null,
      actif: false,
    };
  }
}
