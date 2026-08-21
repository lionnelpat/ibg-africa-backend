import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IParametre, NewParametre } from '../parametre.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IParametre for edit and NewParametreFormGroupInput for create.
 */
type ParametreFormGroupInput = IParametre | PartialWithRequiredKeyOf<NewParametre>;

type ParametreFormDefaults = Pick<NewParametre, 'id' | 'modifiableUi'>;

type ParametreFormGroupContent = {
  id: FormControl<IParametre['id'] | NewParametre['id']>;
  cle: FormControl<IParametre['cle']>;
  libelle: FormControl<IParametre['libelle']>;
  valeur: FormControl<IParametre['valeur']>;
  typeValeur: FormControl<IParametre['typeValeur']>;
  modifiableUi: FormControl<IParametre['modifiableUi']>;
  centre: FormControl<IParametre['centre']>;
};

export type ParametreFormGroup = FormGroup<ParametreFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ParametreFormService {
  createParametreFormGroup(parametre: ParametreFormGroupInput = { id: null }): ParametreFormGroup {
    const parametreRawValue = {
      ...this.getFormDefaults(),
      ...parametre,
    };
    return new FormGroup<ParametreFormGroupContent>({
      id: new FormControl(
        { value: parametreRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      cle: new FormControl(parametreRawValue.cle, {
        validators: [Validators.required, Validators.maxLength(80)],
      }),
      libelle: new FormControl(parametreRawValue.libelle, {
        validators: [Validators.maxLength(255)],
      }),
      valeur: new FormControl(parametreRawValue.valeur, {
        validators: [Validators.maxLength(500)],
      }),
      typeValeur: new FormControl(parametreRawValue.typeValeur, {
        validators: [Validators.required],
      }),
      modifiableUi: new FormControl(parametreRawValue.modifiableUi, {
        validators: [Validators.required],
      }),
      centre: new FormControl(parametreRawValue.centre),
    });
  }

  getParametre(form: ParametreFormGroup): IParametre | NewParametre {
    return form.getRawValue() as IParametre | NewParametre;
  }

  resetForm(form: ParametreFormGroup, parametre: ParametreFormGroupInput): void {
    const parametreRawValue = { ...this.getFormDefaults(), ...parametre };
    form.reset(
      {
        ...parametreRawValue,
        id: { value: parametreRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ParametreFormDefaults {
    return {
      id: null,
      modifiableUi: false,
    };
  }
}
