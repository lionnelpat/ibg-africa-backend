import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPays } from 'app/entities/pays/pays.model';
import { PaysService } from 'app/entities/pays/service/pays.service';
import { IEtudiant } from '../etudiant.model';
import { EtudiantService } from '../service/etudiant.service';
import { EtudiantFormGroup, EtudiantFormService } from './etudiant-form.service';

@Component({
  selector: 'jhi-etudiant-update',
  templateUrl: './etudiant-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class EtudiantUpdateComponent implements OnInit {
  isSaving = false;
  etudiant: IEtudiant | null = null;

  paysSharedCollection: IPays[] = [];

  protected etudiantService = inject(EtudiantService);
  protected etudiantFormService = inject(EtudiantFormService);
  protected paysService = inject(PaysService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: EtudiantFormGroup = this.etudiantFormService.createEtudiantFormGroup();

  comparePays = (o1: IPays | null, o2: IPays | null): boolean => this.paysService.comparePays(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ etudiant }) => {
      this.etudiant = etudiant;
      if (etudiant) {
        this.updateForm(etudiant);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const etudiant = this.etudiantFormService.getEtudiant(this.editForm);
    if (etudiant.id !== null) {
      this.subscribeToSaveResponse(this.etudiantService.update(etudiant));
    } else {
      this.subscribeToSaveResponse(this.etudiantService.create(etudiant));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEtudiant>>): void {
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

  protected updateForm(etudiant: IEtudiant): void {
    this.etudiant = etudiant;
    this.etudiantFormService.resetForm(this.editForm, etudiant);

    this.paysSharedCollection = this.paysService.addPaysToCollectionIfMissing<IPays>(this.paysSharedCollection, etudiant.pays);
  }

  protected loadRelationshipsOptions(): void {
    this.paysService
      .query()
      .pipe(map((res: HttpResponse<IPays[]>) => res.body ?? []))
      .pipe(map((pays: IPays[]) => this.paysService.addPaysToCollectionIfMissing<IPays>(pays, this.etudiant?.pays)))
      .subscribe((pays: IPays[]) => (this.paysSharedCollection = pays));
  }
}
