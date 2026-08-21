import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ICours, NewCours } from '../cours.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICours for edit and NewCoursFormGroupInput for create.
 */
type CoursFormGroupInput = ICours | PartialWithRequiredKeyOf<NewCours>;

type CoursFormDefaults = Pick<NewCours, 'id' | 'actif'>;

type CoursFormGroupContent = {
  id: FormControl<ICours['id'] | NewCours['id']>;
  intitule: FormControl<ICours['intitule']>;
  libelleLong: FormControl<ICours['libelleLong']>;
  libelleCourt: FormControl<ICours['libelleCourt']>;
  ordreAffichage: FormControl<ICours['ordreAffichage']>;
  nbPeriodes: FormControl<ICours['nbPeriodes']>;
  coefficient: FormControl<ICours['coefficient']>;
  dateDebut: FormControl<ICours['dateDebut']>;
  dateFin: FormControl<ICours['dateFin']>;
  commentaire: FormControl<ICours['commentaire']>;
  actif: FormControl<ICours['actif']>;
};

export type CoursFormGroup = FormGroup<CoursFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CoursFormService {
  createCoursFormGroup(cours: CoursFormGroupInput = { id: null }): CoursFormGroup {
    const coursRawValue = {
      ...this.getFormDefaults(),
      ...cours,
    };
    return new FormGroup<CoursFormGroupContent>({
      id: new FormControl(
        { value: coursRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      intitule: new FormControl(coursRawValue.intitule, {
        validators: [Validators.required, Validators.maxLength(150)],
      }),
      libelleLong: new FormControl(coursRawValue.libelleLong, {
        validators: [Validators.maxLength(150)],
      }),
      libelleCourt: new FormControl(coursRawValue.libelleCourt, {
        validators: [Validators.maxLength(60)],
      }),
      ordreAffichage: new FormControl(coursRawValue.ordreAffichage, {
        validators: [Validators.required],
      }),
      nbPeriodes: new FormControl(coursRawValue.nbPeriodes, {
        validators: [Validators.min(0)],
      }),
      coefficient: new FormControl(coursRawValue.coefficient, {
        validators: [Validators.required, Validators.min(0)],
      }),
      dateDebut: new FormControl(coursRawValue.dateDebut),
      dateFin: new FormControl(coursRawValue.dateFin),
      commentaire: new FormControl(coursRawValue.commentaire, {
        validators: [Validators.maxLength(255)],
      }),
      actif: new FormControl(coursRawValue.actif, {
        validators: [Validators.required],
      }),
    });
  }

  getCours(form: CoursFormGroup): ICours | NewCours {
    return form.getRawValue() as ICours | NewCours;
  }

  resetForm(form: CoursFormGroup, cours: CoursFormGroupInput): void {
    const coursRawValue = { ...this.getFormDefaults(), ...cours };
    form.reset(
      {
        ...coursRawValue,
        id: { value: coursRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CoursFormDefaults {
    return {
      id: null,
      actif: false,
    };
  }
}
