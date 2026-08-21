import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IEvenementEtudiant, NewEvenementEtudiant } from '../evenement-etudiant.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEvenementEtudiant for edit and NewEvenementEtudiantFormGroupInput for create.
 */
type EvenementEtudiantFormGroupInput = IEvenementEtudiant | PartialWithRequiredKeyOf<NewEvenementEtudiant>;

type EvenementEtudiantFormDefaults = Pick<NewEvenementEtudiant, 'id'>;

type EvenementEtudiantFormGroupContent = {
  id: FormControl<IEvenementEtudiant['id'] | NewEvenementEtudiant['id']>;
  dateEvenement: FormControl<IEvenementEtudiant['dateEvenement']>;
  intitule: FormControl<IEvenementEtudiant['intitule']>;
  commentaire: FormControl<IEvenementEtudiant['commentaire']>;
  etudiant: FormControl<IEvenementEtudiant['etudiant']>;
};

export type EvenementEtudiantFormGroup = FormGroup<EvenementEtudiantFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EvenementEtudiantFormService {
  createEvenementEtudiantFormGroup(evenementEtudiant: EvenementEtudiantFormGroupInput = { id: null }): EvenementEtudiantFormGroup {
    const evenementEtudiantRawValue = {
      ...this.getFormDefaults(),
      ...evenementEtudiant,
    };
    return new FormGroup<EvenementEtudiantFormGroupContent>({
      id: new FormControl(
        { value: evenementEtudiantRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      dateEvenement: new FormControl(evenementEtudiantRawValue.dateEvenement),
      intitule: new FormControl(evenementEtudiantRawValue.intitule, {
        validators: [Validators.required, Validators.maxLength(150)],
      }),
      commentaire: new FormControl(evenementEtudiantRawValue.commentaire, {
        validators: [Validators.maxLength(255)],
      }),
      etudiant: new FormControl(evenementEtudiantRawValue.etudiant, {
        validators: [Validators.required],
      }),
    });
  }

  getEvenementEtudiant(form: EvenementEtudiantFormGroup): IEvenementEtudiant | NewEvenementEtudiant {
    return form.getRawValue() as IEvenementEtudiant | NewEvenementEtudiant;
  }

  resetForm(form: EvenementEtudiantFormGroup, evenementEtudiant: EvenementEtudiantFormGroupInput): void {
    const evenementEtudiantRawValue = { ...this.getFormDefaults(), ...evenementEtudiant };
    form.reset(
      {
        ...evenementEtudiantRawValue,
        id: { value: evenementEtudiantRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): EvenementEtudiantFormDefaults {
    return {
      id: null,
    };
  }
}
