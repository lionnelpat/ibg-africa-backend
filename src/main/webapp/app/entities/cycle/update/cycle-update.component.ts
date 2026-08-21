import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICentreFormation } from 'app/entities/centre-formation/centre-formation.model';
import { CentreFormationService } from 'app/entities/centre-formation/service/centre-formation.service';
import { ICycle } from '../cycle.model';
import { CycleService } from '../service/cycle.service';
import { CycleFormGroup, CycleFormService } from './cycle-form.service';

@Component({
  selector: 'jhi-cycle-update',
  templateUrl: './cycle-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CycleUpdateComponent implements OnInit {
  isSaving = false;
  cycle: ICycle | null = null;

  centreFormationsSharedCollection: ICentreFormation[] = [];

  protected cycleService = inject(CycleService);
  protected cycleFormService = inject(CycleFormService);
  protected centreFormationService = inject(CentreFormationService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CycleFormGroup = this.cycleFormService.createCycleFormGroup();

  compareCentreFormation = (o1: ICentreFormation | null, o2: ICentreFormation | null): boolean =>
    this.centreFormationService.compareCentreFormation(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cycle }) => {
      this.cycle = cycle;
      if (cycle) {
        this.updateForm(cycle);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cycle = this.cycleFormService.getCycle(this.editForm);
    if (cycle.id !== null) {
      this.subscribeToSaveResponse(this.cycleService.update(cycle));
    } else {
      this.subscribeToSaveResponse(this.cycleService.create(cycle));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICycle>>): void {
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

  protected updateForm(cycle: ICycle): void {
    this.cycle = cycle;
    this.cycleFormService.resetForm(this.editForm, cycle);

    this.centreFormationsSharedCollection = this.centreFormationService.addCentreFormationToCollectionIfMissing<ICentreFormation>(
      this.centreFormationsSharedCollection,
      cycle.centre,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.centreFormationService
      .query()
      .pipe(map((res: HttpResponse<ICentreFormation[]>) => res.body ?? []))
      .pipe(
        map((centreFormations: ICentreFormation[]) =>
          this.centreFormationService.addCentreFormationToCollectionIfMissing<ICentreFormation>(centreFormations, this.cycle?.centre),
        ),
      )
      .subscribe((centreFormations: ICentreFormation[]) => (this.centreFormationsSharedCollection = centreFormations));
  }
}
