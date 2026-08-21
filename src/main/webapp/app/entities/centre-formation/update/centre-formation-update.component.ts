import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IPays } from 'app/entities/pays/pays.model';
import { PaysService } from 'app/entities/pays/service/pays.service';
import { CentreFormationService } from '../service/centre-formation.service';
import { ICentreFormation } from '../centre-formation.model';
import { CentreFormationFormGroup, CentreFormationFormService } from './centre-formation-form.service';

@Component({
  selector: 'jhi-centre-formation-update',
  templateUrl: './centre-formation-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CentreFormationUpdateComponent implements OnInit {
  isSaving = false;
  centreFormation: ICentreFormation | null = null;

  paysSharedCollection: IPays[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected centreFormationService = inject(CentreFormationService);
  protected centreFormationFormService = inject(CentreFormationFormService);
  protected paysService = inject(PaysService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CentreFormationFormGroup = this.centreFormationFormService.createCentreFormationFormGroup();

  comparePays = (o1: IPays | null, o2: IPays | null): boolean => this.paysService.comparePays(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ centreFormation }) => {
      this.centreFormation = centreFormation;
      if (centreFormation) {
        this.updateForm(centreFormation);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('forbidecBackendApp.error', { ...err, key: `error.file.${err.key}` })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const centreFormation = this.centreFormationFormService.getCentreFormation(this.editForm);
    if (centreFormation.id !== null) {
      this.subscribeToSaveResponse(this.centreFormationService.update(centreFormation));
    } else {
      this.subscribeToSaveResponse(this.centreFormationService.create(centreFormation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICentreFormation>>): void {
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

  protected updateForm(centreFormation: ICentreFormation): void {
    this.centreFormation = centreFormation;
    this.centreFormationFormService.resetForm(this.editForm, centreFormation);

    this.paysSharedCollection = this.paysService.addPaysToCollectionIfMissing<IPays>(this.paysSharedCollection, centreFormation.pays);
  }

  protected loadRelationshipsOptions(): void {
    this.paysService
      .query()
      .pipe(map((res: HttpResponse<IPays[]>) => res.body ?? []))
      .pipe(map((pays: IPays[]) => this.paysService.addPaysToCollectionIfMissing<IPays>(pays, this.centreFormation?.pays)))
      .subscribe((pays: IPays[]) => (this.paysSharedCollection = pays));
  }
}
