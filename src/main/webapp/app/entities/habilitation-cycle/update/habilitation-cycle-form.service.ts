import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IHabilitationCycle, NewHabilitationCycle } from '../habilitation-cycle.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IHabilitationCycle for edit and NewHabilitationCycleFormGroupInput for create.
 */
type HabilitationCycleFormGroupInput = IHabilitationCycle | PartialWithRequiredKeyOf<NewHabilitationCycle>;

type HabilitationCycleFormDefaults = Pick<NewHabilitationCycle, 'id'>;

type HabilitationCycleFormGroupContent = {
  id: FormControl<IHabilitationCycle['id'] | NewHabilitationCycle['id']>;
  keycloakUserId: FormControl<IHabilitationCycle['keycloakUserId']>;
  roleFonctionnel: FormControl<IHabilitationCycle['roleFonctionnel']>;
  dateDebut: FormControl<IHabilitationCycle['dateDebut']>;
  dateFin: FormControl<IHabilitationCycle['dateFin']>;
  centre: FormControl<IHabilitationCycle['centre']>;
  cycle: FormControl<IHabilitationCycle['cycle']>;
};

export type HabilitationCycleFormGroup = FormGroup<HabilitationCycleFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class HabilitationCycleFormService {
  createHabilitationCycleFormGroup(habilitationCycle: HabilitationCycleFormGroupInput = { id: null }): HabilitationCycleFormGroup {
    const habilitationCycleRawValue = {
      ...this.getFormDefaults(),
      ...habilitationCycle,
    };
    return new FormGroup<HabilitationCycleFormGroupContent>({
      id: new FormControl(
        { value: habilitationCycleRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      keycloakUserId: new FormControl(habilitationCycleRawValue.keycloakUserId, {
        validators: [Validators.required, Validators.maxLength(64)],
      }),
      roleFonctionnel: new FormControl(habilitationCycleRawValue.roleFonctionnel, {
        validators: [Validators.required],
      }),
      dateDebut: new FormControl(habilitationCycleRawValue.dateDebut),
      dateFin: new FormControl(habilitationCycleRawValue.dateFin),
      centre: new FormControl(habilitationCycleRawValue.centre),
      cycle: new FormControl(habilitationCycleRawValue.cycle),
    });
  }

  getHabilitationCycle(form: HabilitationCycleFormGroup): IHabilitationCycle | NewHabilitationCycle {
    return form.getRawValue() as IHabilitationCycle | NewHabilitationCycle;
  }

  resetForm(form: HabilitationCycleFormGroup, habilitationCycle: HabilitationCycleFormGroupInput): void {
    const habilitationCycleRawValue = { ...this.getFormDefaults(), ...habilitationCycle };
    form.reset(
      {
        ...habilitationCycleRawValue,
        id: { value: habilitationCycleRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): HabilitationCycleFormDefaults {
    return {
      id: null,
    };
  }
}
