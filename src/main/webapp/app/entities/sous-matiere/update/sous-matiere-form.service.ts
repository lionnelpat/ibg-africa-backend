import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ISousMatiere, NewSousMatiere } from '../sous-matiere.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISousMatiere for edit and NewSousMatiereFormGroupInput for create.
 */
type SousMatiereFormGroupInput = ISousMatiere | PartialWithRequiredKeyOf<NewSousMatiere>;

type SousMatiereFormDefaults = Pick<NewSousMatiere, 'id' | 'actif'>;

type SousMatiereFormGroupContent = {
  id: FormControl<ISousMatiere['id'] | NewSousMatiere['id']>;
  intitule: FormControl<ISousMatiere['intitule']>;
  libelleLong: FormControl<ISousMatiere['libelleLong']>;
  libelleCourt: FormControl<ISousMatiere['libelleCourt']>;
  commentaire: FormControl<ISousMatiere['commentaire']>;
  actif: FormControl<ISousMatiere['actif']>;
};

export type SousMatiereFormGroup = FormGroup<SousMatiereFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SousMatiereFormService {
  createSousMatiereFormGroup(sousMatiere: SousMatiereFormGroupInput = { id: null }): SousMatiereFormGroup {
    const sousMatiereRawValue = {
      ...this.getFormDefaults(),
      ...sousMatiere,
    };
    return new FormGroup<SousMatiereFormGroupContent>({
      id: new FormControl(
        { value: sousMatiereRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      intitule: new FormControl(sousMatiereRawValue.intitule, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      libelleLong: new FormControl(sousMatiereRawValue.libelleLong, {
        validators: [Validators.maxLength(100)],
      }),
      libelleCourt: new FormControl(sousMatiereRawValue.libelleCourt, {
        validators: [Validators.maxLength(50)],
      }),
      commentaire: new FormControl(sousMatiereRawValue.commentaire, {
        validators: [Validators.maxLength(255)],
      }),
      actif: new FormControl(sousMatiereRawValue.actif, {
        validators: [Validators.required],
      }),
    });
  }

  getSousMatiere(form: SousMatiereFormGroup): ISousMatiere | NewSousMatiere {
    return form.getRawValue() as ISousMatiere | NewSousMatiere;
  }

  resetForm(form: SousMatiereFormGroup, sousMatiere: SousMatiereFormGroupInput): void {
    const sousMatiereRawValue = { ...this.getFormDefaults(), ...sousMatiere };
    form.reset(
      {
        ...sousMatiereRawValue,
        id: { value: sousMatiereRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SousMatiereFormDefaults {
    return {
      id: null,
      actif: false,
    };
  }
}
