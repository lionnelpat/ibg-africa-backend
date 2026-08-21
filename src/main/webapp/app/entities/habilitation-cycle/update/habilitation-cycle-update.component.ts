import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICentreFormation } from 'app/entities/centre-formation/centre-formation.model';
import { CentreFormationService } from 'app/entities/centre-formation/service/centre-formation.service';
import { ICycle } from 'app/entities/cycle/cycle.model';
import { CycleService } from 'app/entities/cycle/service/cycle.service';
import { RoleFonctionnel } from 'app/entities/enumerations/role-fonctionnel.model';
import { HabilitationCycleService } from '../service/habilitation-cycle.service';
import { IHabilitationCycle } from '../habilitation-cycle.model';
import { HabilitationCycleFormGroup, HabilitationCycleFormService } from './habilitation-cycle-form.service';

@Component({
  selector: 'jhi-habilitation-cycle-update',
  templateUrl: './habilitation-cycle-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class HabilitationCycleUpdateComponent implements OnInit {
  isSaving = false;
  habilitationCycle: IHabilitationCycle | null = null;
  roleFonctionnelValues = Object.keys(RoleFonctionnel);

  centreFormationsSharedCollection: ICentreFormation[] = [];
  cyclesSharedCollection: ICycle[] = [];

  protected habilitationCycleService = inject(HabilitationCycleService);
  protected habilitationCycleFormService = inject(HabilitationCycleFormService);
  protected centreFormationService = inject(CentreFormationService);
  protected cycleService = inject(CycleService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: HabilitationCycleFormGroup = this.habilitationCycleFormService.createHabilitationCycleFormGroup();

  compareCentreFormation = (o1: ICentreFormation | null, o2: ICentreFormation | null): boolean =>
    this.centreFormationService.compareCentreFormation(o1, o2);

  compareCycle = (o1: ICycle | null, o2: ICycle | null): boolean => this.cycleService.compareCycle(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ habilitationCycle }) => {
      this.habilitationCycle = habilitationCycle;
      if (habilitationCycle) {
        this.updateForm(habilitationCycle);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const habilitationCycle = this.habilitationCycleFormService.getHabilitationCycle(this.editForm);
    if (habilitationCycle.id !== null) {
      this.subscribeToSaveResponse(this.habilitationCycleService.update(habilitationCycle));
    } else {
      this.subscribeToSaveResponse(this.habilitationCycleService.create(habilitationCycle));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHabilitationCycle>>): void {
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

  protected updateForm(habilitationCycle: IHabilitationCycle): void {
    this.habilitationCycle = habilitationCycle;
    this.habilitationCycleFormService.resetForm(this.editForm, habilitationCycle);

    this.centreFormationsSharedCollection = this.centreFormationService.addCentreFormationToCollectionIfMissing<ICentreFormation>(
      this.centreFormationsSharedCollection,
      habilitationCycle.centre,
    );
    this.cyclesSharedCollection = this.cycleService.addCycleToCollectionIfMissing<ICycle>(
      this.cyclesSharedCollection,
      habilitationCycle.cycle,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.centreFormationService
      .query()
      .pipe(map((res: HttpResponse<ICentreFormation[]>) => res.body ?? []))
      .pipe(
        map((centreFormations: ICentreFormation[]) =>
          this.centreFormationService.addCentreFormationToCollectionIfMissing<ICentreFormation>(
            centreFormations,
            this.habilitationCycle?.centre,
          ),
        ),
      )
      .subscribe((centreFormations: ICentreFormation[]) => (this.centreFormationsSharedCollection = centreFormations));

    this.cycleService
      .query()
      .pipe(map((res: HttpResponse<ICycle[]>) => res.body ?? []))
      .pipe(map((cycles: ICycle[]) => this.cycleService.addCycleToCollectionIfMissing<ICycle>(cycles, this.habilitationCycle?.cycle)))
      .subscribe((cycles: ICycle[]) => (this.cyclesSharedCollection = cycles));
  }
}
