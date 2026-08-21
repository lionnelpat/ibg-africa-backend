import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IInscriptionCycle, NewInscriptionCycle } from '../inscription-cycle.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInscriptionCycle for edit and NewInscriptionCycleFormGroupInput for create.
 */
type InscriptionCycleFormGroupInput = IInscriptionCycle | PartialWithRequiredKeyOf<NewInscriptionCycle>;

type InscriptionCycleFormDefaults = Pick<NewInscriptionCycle, 'id' | 'cycleTermine'>;

type InscriptionCycleFormGroupContent = {
  id: FormControl<IInscriptionCycle['id'] | NewInscriptionCycle['id']>;
  dateInscription: FormControl<IInscriptionCycle['dateInscription']>;
  cycleTermine: FormControl<IInscriptionCycle['cycleTermine']>;
  groupe: FormControl<IInscriptionCycle['groupe']>;
  commentaire1: FormControl<IInscriptionCycle['commentaire1']>;
  commentaire2: FormControl<IInscriptionCycle['commentaire2']>;
  commentaire3: FormControl<IInscriptionCycle['commentaire3']>;
  commentaire5: FormControl<IInscriptionCycle['commentaire5']>;
  cycle: FormControl<IInscriptionCycle['cycle']>;
  etudiant: FormControl<IInscriptionCycle['etudiant']>;
};

export type InscriptionCycleFormGroup = FormGroup<InscriptionCycleFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class InscriptionCycleFormService {
  createInscriptionCycleFormGroup(inscriptionCycle: InscriptionCycleFormGroupInput = { id: null }): InscriptionCycleFormGroup {
    const inscriptionCycleRawValue = {
      ...this.getFormDefaults(),
      ...inscriptionCycle,
    };
    return new FormGroup<InscriptionCycleFormGroupContent>({
      id: new FormControl(
        { value: inscriptionCycleRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      dateInscription: new FormControl(inscriptionCycleRawValue.dateInscription),
      cycleTermine: new FormControl(inscriptionCycleRawValue.cycleTermine, {
        validators: [Validators.required],
      }),
      groupe: new FormControl(inscriptionCycleRawValue.groupe, {
        validators: [Validators.maxLength(100)],
      }),
      commentaire1: new FormControl(inscriptionCycleRawValue.commentaire1, {
        validators: [Validators.maxLength(255)],
      }),
      commentaire2: new FormControl(inscriptionCycleRawValue.commentaire2, {
        validators: [Validators.maxLength(255)],
      }),
      commentaire3: new FormControl(inscriptionCycleRawValue.commentaire3, {
        validators: [Validators.maxLength(255)],
      }),
      commentaire5: new FormControl(inscriptionCycleRawValue.commentaire5, {
        validators: [Validators.maxLength(255)],
      }),
      cycle: new FormControl(inscriptionCycleRawValue.cycle, {
        validators: [Validators.required],
      }),
      etudiant: new FormControl(inscriptionCycleRawValue.etudiant, {
        validators: [Validators.required],
      }),
    });
  }

  getInscriptionCycle(form: InscriptionCycleFormGroup): IInscriptionCycle | NewInscriptionCycle {
    return form.getRawValue() as IInscriptionCycle | NewInscriptionCycle;
  }

  resetForm(form: InscriptionCycleFormGroup, inscriptionCycle: InscriptionCycleFormGroupInput): void {
    const inscriptionCycleRawValue = { ...this.getFormDefaults(), ...inscriptionCycle };
    form.reset(
      {
        ...inscriptionCycleRawValue,
        id: { value: inscriptionCycleRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): InscriptionCycleFormDefaults {
    return {
      id: null,
      cycleTermine: false,
    };
  }
}
