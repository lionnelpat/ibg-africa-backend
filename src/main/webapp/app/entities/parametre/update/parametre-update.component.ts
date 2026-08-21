import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICentreFormation } from 'app/entities/centre-formation/centre-formation.model';
import { CentreFormationService } from 'app/entities/centre-formation/service/centre-formation.service';
import { TypeValeur } from 'app/entities/enumerations/type-valeur.model';
import { ParametreService } from '../service/parametre.service';
import { IParametre } from '../parametre.model';
import { ParametreFormGroup, ParametreFormService } from './parametre-form.service';

@Component({
  selector: 'jhi-parametre-update',
  templateUrl: './parametre-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ParametreUpdateComponent implements OnInit {
  isSaving = false;
  parametre: IParametre | null = null;
  typeValeurValues = Object.keys(TypeValeur);

  centreFormationsSharedCollection: ICentreFormation[] = [];

  protected parametreService = inject(ParametreService);
  protected parametreFormService = inject(ParametreFormService);
  protected centreFormationService = inject(CentreFormationService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ParametreFormGroup = this.parametreFormService.createParametreFormGroup();

  compareCentreFormation = (o1: ICentreFormation | null, o2: ICentreFormation | null): boolean =>
    this.centreFormationService.compareCentreFormation(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ parametre }) => {
      this.parametre = parametre;
      if (parametre) {
        this.updateForm(parametre);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const parametre = this.parametreFormService.getParametre(this.editForm);
    if (parametre.id !== null) {
      this.subscribeToSaveResponse(this.parametreService.update(parametre));
    } else {
      this.subscribeToSaveResponse(this.parametreService.create(parametre));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IParametre>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(parametre: IParametre): void {
    this.parametre = parametre;
    this.parametreFormService.resetForm(this.editForm, parametre);

    this.centreFormationsSharedCollection = this.centreFormationService.addCentreFormationToCollectionIfMissing<ICentreFormation>(
      this.centreFormationsSharedCollection,
      parametre.centre,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.centreFormationService
      .query()
      .pipe(map((res: HttpResponse<ICentreFormation[]>) => res.body ?? []))
      .pipe(
        map((centreFormations: ICentreFormation[]) =>
          this.centreFormationService.addCentreFormationToCollectionIfMissing<ICentreFormation>(centreFormations, this.parametre?.centre),
        ),
      )
      .subscribe((centreFormations: ICentreFormation[]) => (this.centreFormationsSharedCollection = centreFormations));
  }
}
