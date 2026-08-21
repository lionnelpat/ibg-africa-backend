import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IEvaluationPrevue } from 'app/entities/evaluation-prevue/evaluation-prevue.model';
import { EvaluationPrevueService } from 'app/entities/evaluation-prevue/service/evaluation-prevue.service';
import { IEtudiant } from 'app/entities/etudiant/etudiant.model';
import { EtudiantService } from 'app/entities/etudiant/service/etudiant.service';
import { StatutNote } from 'app/entities/enumerations/statut-note.model';
import { EvaluationRealiseeService } from '../service/evaluation-realisee.service';
import { IEvaluationRealisee } from '../evaluation-realisee.model';
import { EvaluationRealiseeFormGroup, EvaluationRealiseeFormService } from './evaluation-realisee-form.service';

@Component({
  selector: 'jhi-evaluation-realisee-update',
  templateUrl: './evaluation-realisee-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class EvaluationRealiseeUpdateComponent implements OnInit {
  isSaving = false;
  evaluationRealisee: IEvaluationRealisee | null = null;
  statutNoteValues = Object.keys(StatutNote);

  evaluationPrevuesSharedCollection: IEvaluationPrevue[] = [];
  etudiantsSharedCollection: IEtudiant[] = [];

  protected evaluationRealiseeService = inject(EvaluationRealiseeService);
  protected evaluationRealiseeFormService = inject(EvaluationRealiseeFormService);
  protected evaluationPrevueService = inject(EvaluationPrevueService);
  protected etudiantService = inject(EtudiantService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: EvaluationRealiseeFormGroup = this.evaluationRealiseeFormService.createEvaluationRealiseeFormGroup();

  compareEvaluationPrevue = (o1: IEvaluationPrevue | null, o2: IEvaluationPrevue | null): boolean =>
    this.evaluationPrevueService.compareEvaluationPrevue(o1, o2);

  compareEtudiant = (o1: IEtudiant | null, o2: IEtudiant | null): boolean => this.etudiantService.compareEtudiant(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ evaluationRealisee }) => {
      this.evaluationRealisee = evaluationRealisee;
      if (evaluationRealisee) {
        this.updateForm(evaluationRealisee);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const evaluationRealisee = this.evaluationRealiseeFormService.getEvaluationRealisee(this.editForm);
    if (evaluationRealisee.id !== null) {
      this.subscribeToSaveResponse(this.evaluationRealiseeService.update(evaluationRealisee));
    } else {
      this.subscribeToSaveResponse(this.evaluationRealiseeService.create(evaluationRealisee));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEvaluationRealisee>>): void {
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

  protected updateForm(evaluationRealisee: IEvaluationRealisee): void {
    this.evaluationRealisee = evaluationRealisee;
    this.evaluationRealiseeFormService.resetForm(this.editForm, evaluationRealisee);

    this.evaluationPrevuesSharedCollection = this.evaluationPrevueService.addEvaluationPrevueToCollectionIfMissing<IEvaluationPrevue>(
      this.evaluationPrevuesSharedCollection,
      evaluationRealisee.evaluationPrevue,
    );
    this.etudiantsSharedCollection = this.etudiantService.addEtudiantToCollectionIfMissing<IEtudiant>(
      this.etudiantsSharedCollection,
      evaluationRealisee.etudiant,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.evaluationPrevueService
      .query()
      .pipe(map((res: HttpResponse<IEvaluationPrevue[]>) => res.body ?? []))
      .pipe(
        map((evaluationPrevues: IEvaluationPrevue[]) =>
          this.evaluationPrevueService.addEvaluationPrevueToCollectionIfMissing<IEvaluationPrevue>(
            evaluationPrevues,
            this.evaluationRealisee?.evaluationPrevue,
          ),
        ),
      )
      .subscribe((evaluationPrevues: IEvaluationPrevue[]) => (this.evaluationPrevuesSharedCollection = evaluationPrevues));

    this.etudiantService
      .query()
      .pipe(map((res: HttpResponse<IEtudiant[]>) => res.body ?? []))
      .pipe(
        map((etudiants: IEtudiant[]) =>
          this.etudiantService.addEtudiantToCollectionIfMissing<IEtudiant>(etudiants, this.evaluationRealisee?.etudiant),
        ),
      )
      .subscribe((etudiants: IEtudiant[]) => (this.etudiantsSharedCollection = etudiants));
  }
}
