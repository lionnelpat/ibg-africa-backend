import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IEvaluationRealisee, NewEvaluationRealisee } from '../evaluation-realisee.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEvaluationRealisee for edit and NewEvaluationRealiseeFormGroupInput for create.
 */
type EvaluationRealiseeFormGroupInput = IEvaluationRealisee | PartialWithRequiredKeyOf<NewEvaluationRealisee>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IEvaluationRealisee | NewEvaluationRealisee> = Omit<T, 'saisieLe' | 'valideeLe'> & {
  saisieLe?: string | null;
  valideeLe?: string | null;
};

type EvaluationRealiseeFormRawValue = FormValueOf<IEvaluationRealisee>;

type NewEvaluationRealiseeFormRawValue = FormValueOf<NewEvaluationRealisee>;

type EvaluationRealiseeFormDefaults = Pick<NewEvaluationRealisee, 'id' | 'compteDansMoyenne' | 'saisieLe' | 'valideeLe'>;

type EvaluationRealiseeFormGroupContent = {
  id: FormControl<EvaluationRealiseeFormRawValue['id'] | NewEvaluationRealisee['id']>;
  note: FormControl<EvaluationRealiseeFormRawValue['note']>;
  statut: FormControl<EvaluationRealiseeFormRawValue['statut']>;
  compteDansMoyenne: FormControl<EvaluationRealiseeFormRawValue['compteDansMoyenne']>;
  dateDebut: FormControl<EvaluationRealiseeFormRawValue['dateDebut']>;
  dateFin: FormControl<EvaluationRealiseeFormRawValue['dateFin']>;
  commentaire1: FormControl<EvaluationRealiseeFormRawValue['commentaire1']>;
  commentaire2: FormControl<EvaluationRealiseeFormRawValue['commentaire2']>;
  commentaire3: FormControl<EvaluationRealiseeFormRawValue['commentaire3']>;
  saisiePar: FormControl<EvaluationRealiseeFormRawValue['saisiePar']>;
  saisieLe: FormControl<EvaluationRealiseeFormRawValue['saisieLe']>;
  valideePar: FormControl<EvaluationRealiseeFormRawValue['valideePar']>;
  valideeLe: FormControl<EvaluationRealiseeFormRawValue['valideeLe']>;
  evaluationPrevue: FormControl<EvaluationRealiseeFormRawValue['evaluationPrevue']>;
  etudiant: FormControl<EvaluationRealiseeFormRawValue['etudiant']>;
};

export type EvaluationRealiseeFormGroup = FormGroup<EvaluationRealiseeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EvaluationRealiseeFormService {
  createEvaluationRealiseeFormGroup(evaluationRealisee: EvaluationRealiseeFormGroupInput = { id: null }): EvaluationRealiseeFormGroup {
    const evaluationRealiseeRawValue = this.convertEvaluationRealiseeToEvaluationRealiseeRawValue({
      ...this.getFormDefaults(),
      ...evaluationRealisee,
    });
    return new FormGroup<EvaluationRealiseeFormGroupContent>({
      id: new FormControl(
        { value: evaluationRealiseeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      note: new FormControl(evaluationRealiseeRawValue.note, {
        validators: [Validators.min(0)],
      }),
      statut: new FormControl(evaluationRealiseeRawValue.statut, {
        validators: [Validators.required],
      }),
      compteDansMoyenne: new FormControl(evaluationRealiseeRawValue.compteDansMoyenne, {
        validators: [Validators.required],
      }),
      dateDebut: new FormControl(evaluationRealiseeRawValue.dateDebut),
      dateFin: new FormControl(evaluationRealiseeRawValue.dateFin),
      commentaire1: new FormControl(evaluationRealiseeRawValue.commentaire1, {
        validators: [Validators.maxLength(255)],
      }),
      commentaire2: new FormControl(evaluationRealiseeRawValue.commentaire2, {
        validators: [Validators.maxLength(255)],
      }),
      commentaire3: new FormControl(evaluationRealiseeRawValue.commentaire3, {
        validators: [Validators.maxLength(255)],
      }),
      saisiePar: new FormControl(evaluationRealiseeRawValue.saisiePar, {
        validators: [Validators.maxLength(64)],
      }),
      saisieLe: new FormControl(evaluationRealiseeRawValue.saisieLe),
      valideePar: new FormControl(evaluationRealiseeRawValue.valideePar, {
        validators: [Validators.maxLength(64)],
      }),
      valideeLe: new FormControl(evaluationRealiseeRawValue.valideeLe),
      evaluationPrevue: new FormControl(evaluationRealiseeRawValue.evaluationPrevue, {
        validators: [Validators.required],
      }),
      etudiant: new FormControl(evaluationRealiseeRawValue.etudiant, {
        validators: [Validators.required],
      }),
    });
  }

  getEvaluationRealisee(form: EvaluationRealiseeFormGroup): IEvaluationRealisee | NewEvaluationRealisee {
    return this.convertEvaluationRealiseeRawValueToEvaluationRealisee(
      form.getRawValue() as EvaluationRealiseeFormRawValue | NewEvaluationRealiseeFormRawValue,
    );
  }

  resetForm(form: EvaluationRealiseeFormGroup, evaluationRealisee: EvaluationRealiseeFormGroupInput): void {
    const evaluationRealiseeRawValue = this.convertEvaluationRealiseeToEvaluationRealiseeRawValue({
      ...this.getFormDefaults(),
      ...evaluationRealisee,
    });
    form.reset(
      {
        ...evaluationRealiseeRawValue,
        id: { value: evaluationRealiseeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): EvaluationRealiseeFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      compteDansMoyenne: false,
      saisieLe: currentTime,
      valideeLe: currentTime,
    };
  }

  private convertEvaluationRealiseeRawValueToEvaluationRealisee(
    rawEvaluationRealisee: EvaluationRealiseeFormRawValue | NewEvaluationRealiseeFormRawValue,
  ): IEvaluationRealisee | NewEvaluationRealisee {
    return {
      ...rawEvaluationRealisee,
      saisieLe: dayjs(rawEvaluationRealisee.saisieLe, DATE_TIME_FORMAT),
      valideeLe: dayjs(rawEvaluationRealisee.valideeLe, DATE_TIME_FORMAT),
    };
  }

  private convertEvaluationRealiseeToEvaluationRealiseeRawValue(
    evaluationRealisee: IEvaluationRealisee | (Partial<NewEvaluationRealisee> & EvaluationRealiseeFormDefaults),
  ): EvaluationRealiseeFormRawValue | PartialWithRequiredKeyOf<NewEvaluationRealiseeFormRawValue> {
    return {
      ...evaluationRealisee,
      saisieLe: evaluationRealisee.saisieLe ? evaluationRealisee.saisieLe.format(DATE_TIME_FORMAT) : undefined,
      valideeLe: evaluationRealisee.valideeLe ? evaluationRealisee.valideeLe.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
