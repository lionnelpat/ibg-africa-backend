import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IEvaluationPrevue, NewEvaluationPrevue } from '../evaluation-prevue.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEvaluationPrevue for edit and NewEvaluationPrevueFormGroupInput for create.
 */
type EvaluationPrevueFormGroupInput = IEvaluationPrevue | PartialWithRequiredKeyOf<NewEvaluationPrevue>;

type EvaluationPrevueFormDefaults = Pick<NewEvaluationPrevue, 'id' | 'compteDansMoyenne'>;

type EvaluationPrevueFormGroupContent = {
  id: FormControl<IEvaluationPrevue['id'] | NewEvaluationPrevue['id']>;
  intitule: FormControl<IEvaluationPrevue['intitule']>;
  libelleImpression: FormControl<IEvaluationPrevue['libelleImpression']>;
  coefficient: FormControl<IEvaluationPrevue['coefficient']>;
  compteDansMoyenne: FormControl<IEvaluationPrevue['compteDansMoyenne']>;
  noteMaximale: FormControl<IEvaluationPrevue['noteMaximale']>;
  dateDebut: FormControl<IEvaluationPrevue['dateDebut']>;
  dateFin: FormControl<IEvaluationPrevue['dateFin']>;
  commentaire: FormControl<IEvaluationPrevue['commentaire']>;
  cycle: FormControl<IEvaluationPrevue['cycle']>;
  enseignant: FormControl<IEvaluationPrevue['enseignant']>;
  matiere: FormControl<IEvaluationPrevue['matiere']>;
  sousMatiere: FormControl<IEvaluationPrevue['sousMatiere']>;
  cours: FormControl<IEvaluationPrevue['cours']>;
  typeTache: FormControl<IEvaluationPrevue['typeTache']>;
};

export type EvaluationPrevueFormGroup = FormGroup<EvaluationPrevueFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EvaluationPrevueFormService {
  createEvaluationPrevueFormGroup(evaluationPrevue: EvaluationPrevueFormGroupInput = { id: null }): EvaluationPrevueFormGroup {
    const evaluationPrevueRawValue = {
      ...this.getFormDefaults(),
      ...evaluationPrevue,
    };
    return new FormGroup<EvaluationPrevueFormGroupContent>({
      id: new FormControl(
        { value: evaluationPrevueRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      intitule: new FormControl(evaluationPrevueRawValue.intitule, {
        validators: [Validators.required, Validators.maxLength(150)],
      }),
      libelleImpression: new FormControl(evaluationPrevueRawValue.libelleImpression, {
        validators: [Validators.required, Validators.maxLength(150)],
      }),
      coefficient: new FormControl(evaluationPrevueRawValue.coefficient, {
        validators: [Validators.required, Validators.min(0)],
      }),
      compteDansMoyenne: new FormControl(evaluationPrevueRawValue.compteDansMoyenne, {
        validators: [Validators.required],
      }),
      noteMaximale: new FormControl(evaluationPrevueRawValue.noteMaximale, {
        validators: [Validators.required, Validators.min(1)],
      }),
      dateDebut: new FormControl(evaluationPrevueRawValue.dateDebut),
      dateFin: new FormControl(evaluationPrevueRawValue.dateFin),
      commentaire: new FormControl(evaluationPrevueRawValue.commentaire, {
        validators: [Validators.maxLength(255)],
      }),
      cycle: new FormControl(evaluationPrevueRawValue.cycle, {
        validators: [Validators.required],
      }),
      enseignant: new FormControl(evaluationPrevueRawValue.enseignant, {
        validators: [Validators.required],
      }),
      matiere: new FormControl(evaluationPrevueRawValue.matiere, {
        validators: [Validators.required],
      }),
      sousMatiere: new FormControl(evaluationPrevueRawValue.sousMatiere, {
        validators: [Validators.required],
      }),
      cours: new FormControl(evaluationPrevueRawValue.cours, {
        validators: [Validators.required],
      }),
      typeTache: new FormControl(evaluationPrevueRawValue.typeTache, {
        validators: [Validators.required],
      }),
    });
  }

  getEvaluationPrevue(form: EvaluationPrevueFormGroup): IEvaluationPrevue | NewEvaluationPrevue {
    return form.getRawValue() as IEvaluationPrevue | NewEvaluationPrevue;
  }

  resetForm(form: EvaluationPrevueFormGroup, evaluationPrevue: EvaluationPrevueFormGroupInput): void {
    const evaluationPrevueRawValue = { ...this.getFormDefaults(), ...evaluationPrevue };
    form.reset(
      {
        ...evaluationPrevueRawValue,
        id: { value: evaluationPrevueRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): EvaluationPrevueFormDefaults {
    return {
      id: null,
      compteDansMoyenne: false,
    };
  }
}
