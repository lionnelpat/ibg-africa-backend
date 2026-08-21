import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IPays, NewPays } from '../pays.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPays for edit and NewPaysFormGroupInput for create.
 */
type PaysFormGroupInput = IPays | PartialWithRequiredKeyOf<NewPays>;

type PaysFormDefaults = Pick<NewPays, 'id' | 'actif'>;

type PaysFormGroupContent = {
  id: FormControl<IPays['id'] | NewPays['id']>;
  codeIso: FormControl<IPays['codeIso']>;
  nom: FormControl<IPays['nom']>;
  langue: FormControl<IPays['langue']>;
  fuseau: FormControl<IPays['fuseau']>;
  actif: FormControl<IPays['actif']>;
};

export type PaysFormGroup = FormGroup<PaysFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PaysFormService {
  createPaysFormGroup(pays: PaysFormGroupInput = { id: null }): PaysFormGroup {
    const paysRawValue = {
      ...this.getFormDefaults(),
      ...pays,
    };
    return new FormGroup<PaysFormGroupContent>({
      id: new FormControl(
        { value: paysRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      codeIso: new FormControl(paysRawValue.codeIso, {
        validators: [Validators.required, Validators.minLength(2), Validators.maxLength(2)],
      }),
      nom: new FormControl(paysRawValue.nom, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      langue: new FormControl(paysRawValue.langue, {
        validators: [Validators.required, Validators.maxLength(5)],
      }),
      fuseau: new FormControl(paysRawValue.fuseau, {
        validators: [Validators.maxLength(50)],
      }),
      actif: new FormControl(paysRawValue.actif, {
        validators: [Validators.required],
      }),
    });
  }

  getPays(form: PaysFormGroup): IPays | NewPays {
    return form.getRawValue() as IPays | NewPays;
  }

  resetForm(form: PaysFormGroup, pays: PaysFormGroupInput): void {
    const paysRawValue = { ...this.getFormDefaults(), ...pays };
    form.reset(
      {
        ...paysRawValue,
        id: { value: paysRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PaysFormDefaults {
    return {
      id: null,
      actif: false,
    };
  }
}
