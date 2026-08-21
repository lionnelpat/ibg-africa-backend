import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ICycle, NewCycle } from '../cycle.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICycle for edit and NewCycleFormGroupInput for create.
 */
type CycleFormGroupInput = ICycle | PartialWithRequiredKeyOf<NewCycle>;

type CycleFormDefaults = Pick<NewCycle, 'id' | 'cloture'>;

type CycleFormGroupContent = {
  id: FormControl<ICycle['id'] | NewCycle['id']>;
  annee: FormControl<ICycle['annee']>;
  libelle: FormControl<ICycle['libelle']>;
  dateDebut: FormControl<ICycle['dateDebut']>;
  dateFin: FormControl<ICycle['dateFin']>;
  cloture: FormControl<ICycle['cloture']>;
  commentaire: FormControl<ICycle['commentaire']>;
  centre: FormControl<ICycle['centre']>;
};

export type CycleFormGroup = FormGroup<CycleFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CycleFormService {
  createCycleFormGroup(cycle: CycleFormGroupInput = { id: null }): CycleFormGroup {
    const cycleRawValue = {
      ...this.getFormDefaults(),
      ...cycle,
    };
    return new FormGroup<CycleFormGroupContent>({
      id: new FormControl(
        { value: cycleRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      annee: new FormControl(cycleRawValue.annee, {
        validators: [Validators.required, Validators.min(1900), Validators.max(2200)],
      }),
      libelle: new FormControl(cycleRawValue.libelle, {
        validators: [Validators.maxLength(100)],
      }),
      dateDebut: new FormControl(cycleRawValue.dateDebut),
      dateFin: new FormControl(cycleRawValue.dateFin),
      cloture: new FormControl(cycleRawValue.cloture, {
        validators: [Validators.required],
      }),
      commentaire: new FormControl(cycleRawValue.commentaire, {
        validators: [Validators.maxLength(255)],
      }),
      centre: new FormControl(cycleRawValue.centre, {
        validators: [Validators.required],
      }),
    });
  }

  getCycle(form: CycleFormGroup): ICycle | NewCycle {
    return form.getRawValue() as ICycle | NewCycle;
  }

  resetForm(form: CycleFormGroup, cycle: CycleFormGroupInput): void {
    const cycleRawValue = { ...this.getFormDefaults(), ...cycle };
    form.reset(
      {
        ...cycleRawValue,
        id: { value: cycleRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CycleFormDefaults {
    return {
      id: null,
      cloture: false,
    };
  }
}
