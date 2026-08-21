import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { IBaremeMention, NewBaremeMention } from '../bareme-mention.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBaremeMention for edit and NewBaremeMentionFormGroupInput for create.
 */
type BaremeMentionFormGroupInput = IBaremeMention | PartialWithRequiredKeyOf<NewBaremeMention>;

type BaremeMentionFormDefaults = Pick<NewBaremeMention, 'id' | 'minInclus' | 'maxInclus' | 'actif'>;

type BaremeMentionFormGroupContent = {
  id: FormControl<IBaremeMention['id'] | NewBaremeMention['id']>;
  libelleLong: FormControl<IBaremeMention['libelleLong']>;
  libelleCourt: FormControl<IBaremeMention['libelleCourt']>;
  borneMin: FormControl<IBaremeMention['borneMin']>;
  minInclus: FormControl<IBaremeMention['minInclus']>;
  borneMax: FormControl<IBaremeMention['borneMax']>;
  maxInclus: FormControl<IBaremeMention['maxInclus']>;
  ordreAffichage: FormControl<IBaremeMention['ordreAffichage']>;
  commentaire: FormControl<IBaremeMention['commentaire']>;
  actif: FormControl<IBaremeMention['actif']>;
  centre: FormControl<IBaremeMention['centre']>;
};

export type BaremeMentionFormGroup = FormGroup<BaremeMentionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BaremeMentionFormService {
  createBaremeMentionFormGroup(baremeMention: BaremeMentionFormGroupInput = { id: null }): BaremeMentionFormGroup {
    const baremeMentionRawValue = {
      ...this.getFormDefaults(),
      ...baremeMention,
    };
    return new FormGroup<BaremeMentionFormGroupContent>({
      id: new FormControl(
        { value: baremeMentionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      libelleLong: new FormControl(baremeMentionRawValue.libelleLong, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      libelleCourt: new FormControl(baremeMentionRawValue.libelleCourt, {
        validators: [Validators.required, Validators.maxLength(50)],
      }),
      borneMin: new FormControl(baremeMentionRawValue.borneMin, {
        validators: [Validators.min(0)],
      }),
      minInclus: new FormControl(baremeMentionRawValue.minInclus, {
        validators: [Validators.required],
      }),
      borneMax: new FormControl(baremeMentionRawValue.borneMax, {
        validators: [Validators.min(0)],
      }),
      maxInclus: new FormControl(baremeMentionRawValue.maxInclus, {
        validators: [Validators.required],
      }),
      ordreAffichage: new FormControl(baremeMentionRawValue.ordreAffichage, {
        validators: [Validators.required],
      }),
      commentaire: new FormControl(baremeMentionRawValue.commentaire, {
        validators: [Validators.maxLength(255)],
      }),
      actif: new FormControl(baremeMentionRawValue.actif, {
        validators: [Validators.required],
      }),
      centre: new FormControl(baremeMentionRawValue.centre),
    });
  }

  getBaremeMention(form: BaremeMentionFormGroup): IBaremeMention | NewBaremeMention {
    return form.getRawValue() as IBaremeMention | NewBaremeMention;
  }

  resetForm(form: BaremeMentionFormGroup, baremeMention: BaremeMentionFormGroupInput): void {
    const baremeMentionRawValue = { ...this.getFormDefaults(), ...baremeMention };
    form.reset(
      {
        ...baremeMentionRawValue,
        id: { value: baremeMentionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): BaremeMentionFormDefaults {
    return {
      id: null,
      minInclus: false,
      maxInclus: false,
      actif: false,
    };
  }
}
