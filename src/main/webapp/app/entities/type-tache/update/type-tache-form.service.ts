import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ITypeTache, NewTypeTache } from '../type-tache.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITypeTache for edit and NewTypeTacheFormGroupInput for create.
 */
type TypeTacheFormGroupInput = ITypeTache | PartialWithRequiredKeyOf<NewTypeTache>;

type TypeTacheFormDefaults = Pick<NewTypeTache, 'id' | 'entreDansMoyenne' | 'actif'>;

type TypeTacheFormGroupContent = {
  id: FormControl<ITypeTache['id'] | NewTypeTache['id']>;
  code: FormControl<ITypeTache['code']>;
  intitule: FormControl<ITypeTache['intitule']>;
  libelleLong: FormControl<ITypeTache['libelleLong']>;
  libelleCourt: FormControl<ITypeTache['libelleCourt']>;
  entreDansMoyenne: FormControl<ITypeTache['entreDansMoyenne']>;
  commentaire: FormControl<ITypeTache['commentaire']>;
  actif: FormControl<ITypeTache['actif']>;
};

export type TypeTacheFormGroup = FormGroup<TypeTacheFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TypeTacheFormService {
  createTypeTacheFormGroup(typeTache: TypeTacheFormGroupInput = { id: null }): TypeTacheFormGroup {
    const typeTacheRawValue = {
      ...this.getFormDefaults(),
      ...typeTache,
    };
    return new FormGroup<TypeTacheFormGroupContent>({
      id: new FormControl(
        { value: typeTacheRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      code: new FormControl(typeTacheRawValue.code, {
        validators: [Validators.required, Validators.maxLength(30)],
      }),
      intitule: new FormControl(typeTacheRawValue.intitule, {
        validators: [Validators.required, Validators.maxLength(100)],
      }),
      libelleLong: new FormControl(typeTacheRawValue.libelleLong, {
        validators: [Validators.maxLength(100)],
      }),
      libelleCourt: new FormControl(typeTacheRawValue.libelleCourt, {
        validators: [Validators.maxLength(50)],
      }),
      entreDansMoyenne: new FormControl(typeTacheRawValue.entreDansMoyenne, {
        validators: [Validators.required],
      }),
      commentaire: new FormControl(typeTacheRawValue.commentaire, {
        validators: [Validators.maxLength(255)],
      }),
      actif: new FormControl(typeTacheRawValue.actif, {
        validators: [Validators.required],
      }),
    });
  }

  getTypeTache(form: TypeTacheFormGroup): ITypeTache | NewTypeTache {
    return form.getRawValue() as ITypeTache | NewTypeTache;
  }

  resetForm(form: TypeTacheFormGroup, typeTache: TypeTacheFormGroupInput): void {
    const typeTacheRawValue = { ...this.getFormDefaults(), ...typeTache };
    form.reset(
      {
        ...typeTacheRawValue,
        id: { value: typeTacheRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TypeTacheFormDefaults {
    return {
      id: null,
      entreDansMoyenne: false,
      actif: false,
    };
  }
}
