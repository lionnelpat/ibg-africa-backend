import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICentreFormation } from 'app/entities/centre-formation/centre-formation.model';
import { CentreFormationService } from 'app/entities/centre-formation/service/centre-formation.service';
import { IBaremeMention } from '../bareme-mention.model';
import { BaremeMentionService } from '../service/bareme-mention.service';
import { BaremeMentionFormGroup, BaremeMentionFormService } from './bareme-mention-form.service';

@Component({
  selector: 'jhi-bareme-mention-update',
  templateUrl: './bareme-mention-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class BaremeMentionUpdateComponent implements OnInit {
  isSaving = false;
  baremeMention: IBaremeMention | null = null;

  centreFormationsSharedCollection: ICentreFormation[] = [];

  protected baremeMentionService = inject(BaremeMentionService);
  protected baremeMentionFormService = inject(BaremeMentionFormService);
  protected centreFormationService = inject(CentreFormationService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: BaremeMentionFormGroup = this.baremeMentionFormService.createBaremeMentionFormGroup();

  compareCentreFormation = (o1: ICentreFormation | null, o2: ICentreFormation | null): boolean =>
    this.centreFormationService.compareCentreFormation(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ baremeMention }) => {
      this.baremeMention = baremeMention;
      if (baremeMention) {
        this.updateForm(baremeMention);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const baremeMention = this.baremeMentionFormService.getBaremeMention(this.editForm);
    if (baremeMention.id !== null) {
      this.subscribeToSaveResponse(this.baremeMentionService.update(baremeMention));
    } else {
      this.subscribeToSaveResponse(this.baremeMentionService.create(baremeMention));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBaremeMention>>): void {
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

  protected updateForm(baremeMention: IBaremeMention): void {
    this.baremeMention = baremeMention;
    this.baremeMentionFormService.resetForm(this.editForm, baremeMention);

    this.centreFormationsSharedCollection = this.centreFormationService.addCentreFormationToCollectionIfMissing<ICentreFormation>(
      this.centreFormationsSharedCollection,
      baremeMention.centre,
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
            this.baremeMention?.centre,
          ),
        ),
      )
      .subscribe((centreFormations: ICentreFormation[]) => (this.centreFormationsSharedCollection = centreFormations));
  }
}
