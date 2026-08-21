import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ICentreFormation, NewCentreFormation } from '../centre-formation.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICentreFormation for edit and NewCentreFormationFormGroupInput for create.
 */
type CentreFormationFormGroupInput = ICentreFormation | PartialWithRequiredKeyOf<NewCentreFormation>;

type CentreFormationFormDefaults = Pick<NewCentreFormation, 'id' | 'actif'>;

type CentreFormationFormGroupContent = {
  id: FormControl<ICentreFormation['id'] | NewCentreFormation['id']>;
  code: FormControl<ICentreFormation['code']>;
  nom: FormControl<ICentreFormation['nom']>;
  ville: FormControl<ICentreFormation['ville']>;
  adresse: FormControl<ICentreFormation['adresse']>;
  enteteDocument: FormControl<ICentreFormation['enteteDocument']>;
  signataire: FormControl<ICentreFormation['signataire']>;
  logoUrl: FormControl<ICentreFormation['logoUrl']>;
  nbCyclesCursus: FormControl<ICentreFormation['nbCyclesCursus']>;
  noteMaximale: FormControl<ICentreFormation['noteMaximale']>;
  actif: FormControl<ICentreFormation['actif']>;
  pays: FormControl<ICentreFormation['pays']>;
};

export type CentreFormationFormGroup = FormGroup<CentreFormationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CentreFormationFormService {
  createCentreFormationFormGroup(centreFormation: CentreFormationFormGroupInput = { id: null }): CentreFormationFormGroup {
    const centreFormationRawValue = {
      ...this.getFormDefaults(),
      ...centreFormation,
    };
    return new FormGroup<CentreFormationFormGroupContent>({
      id: new FormControl(
        { value: centreFormationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      code: new FormControl(centreFormationRawValue.code, {
        validators: [Validators.required, Validators.maxLength(20)],
      }),
      nom: new FormControl(centreFormationRawValue.nom, {
        validators: [Validators.required, Validators.maxLength(150)],
      }),
      ville: new FormControl(centreFormationRawValue.ville, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      adresse: new FormControl(centreFormationRawValue.adresse, {
        validators: [Validators.maxLength(255)],
      }),
      enteteDocument: new FormControl(centreFormationRawValue.enteteDocument),
      signataire: new FormControl(centreFormationRawValue.signataire, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      logoUrl: new FormControl(centreFormationRawValue.logoUrl, {
        validators: [Validators.maxLength(255)],
      }),
      nbCyclesCursus: new FormControl(centreFormationRawValue.nbCyclesCursus, {
        validators: [Validators.required, Validators.min(1), Validators.max(20)],
      }),
      noteMaximale: new FormControl(centreFormationRawValue.noteMaximale, {
        validators: [Validators.required, Validators.min(1)],
      }),
      actif: new FormControl(centreFormationRawValue.actif, {
        validators: [Validators.required],
      }),
      pays: new FormControl(centreFormationRawValue.pays, {
        validators: [Validators.required],
      }),
    });
  }

  getCentreFormation(form: CentreFormationFormGroup): ICentreFormation | NewCentreFormation {
    return form.getRawValue() as ICentreFormation | NewCentreFormation;
  }

  resetForm(form: CentreFormationFormGroup, centreFormation: CentreFormationFormGroupInput): void {
    const centreFormationRawValue = { ...this.getFormDefaults(), ...centreFormation };
    form.reset(
      {
        ...centreFormationRawValue,
        id: { value: centreFormationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CentreFormationFormDefaults {
    return {
      id: null,
      actif: false,
    };
  }
}
