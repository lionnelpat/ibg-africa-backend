import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICycle } from 'app/entities/cycle/cycle.model';
import { CycleService } from 'app/entities/cycle/service/cycle.service';
import { IEnseignant } from 'app/entities/enseignant/enseignant.model';
import { EnseignantService } from 'app/entities/enseignant/service/enseignant.service';
import { IMatiere } from 'app/entities/matiere/matiere.model';
import { MatiereService } from 'app/entities/matiere/service/matiere.service';
import { ISousMatiere } from 'app/entities/sous-matiere/sous-matiere.model';
import { SousMatiereService } from 'app/entities/sous-matiere/service/sous-matiere.service';
import { ICours } from 'app/entities/cours/cours.model';
import { CoursService } from 'app/entities/cours/service/cours.service';
import { ITypeTache } from 'app/entities/type-tache/type-tache.model';
import { TypeTacheService } from 'app/entities/type-tache/service/type-tache.service';
import { EvaluationPrevueService } from '../service/evaluation-prevue.service';
import { IEvaluationPrevue } from '../evaluation-prevue.model';
import { EvaluationPrevueFormGroup, EvaluationPrevueFormService } from './evaluation-prevue-form.service';

@Component({
  selector: 'jhi-evaluation-prevue-update',
  templateUrl: './evaluation-prevue-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class EvaluationPrevueUpdateComponent implements OnInit {
  isSaving = false;
  evaluationPrevue: IEvaluationPrevue | null = null;

  cyclesSharedCollection: ICycle[] = [];
  enseignantsSharedCollection: IEnseignant[] = [];
  matieresSharedCollection: IMatiere[] = [];
  sousMatieresSharedCollection: ISousMatiere[] = [];
  coursSharedCollection: ICours[] = [];
  typeTachesSharedCollection: ITypeTache[] = [];

  protected evaluationPrevueService = inject(EvaluationPrevueService);
  protected evaluationPrevueFormService = inject(EvaluationPrevueFormService);
  protected cycleService = inject(CycleService);
  protected enseignantService = inject(EnseignantService);
  protected matiereService = inject(MatiereService);
  protected sousMatiereService = inject(SousMatiereService);
  protected coursService = inject(CoursService);
  protected typeTacheService = inject(TypeTacheService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: EvaluationPrevueFormGroup = this.evaluationPrevueFormService.createEvaluationPrevueFormGroup();

  compareCycle = (o1: ICycle | null, o2: ICycle | null): boolean => this.cycleService.compareCycle(o1, o2);

  compareEnseignant = (o1: IEnseignant | null, o2: IEnseignant | null): boolean => this.enseignantService.compareEnseignant(o1, o2);

  compareMatiere = (o1: IMatiere | null, o2: IMatiere | null): boolean => this.matiereService.compareMatiere(o1, o2);

  compareSousMatiere = (o1: ISousMatiere | null, o2: ISousMatiere | null): boolean => this.sousMatiereService.compareSousMatiere(o1, o2);

  compareCours = (o1: ICours | null, o2: ICours | null): boolean => this.coursService.compareCours(o1, o2);

  compareTypeTache = (o1: ITypeTache | null, o2: ITypeTache | null): boolean => this.typeTacheService.compareTypeTache(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ evaluationPrevue }) => {
      this.evaluationPrevue = evaluationPrevue;
      if (evaluationPrevue) {
        this.updateForm(evaluationPrevue);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const evaluationPrevue = this.evaluationPrevueFormService.getEvaluationPrevue(this.editForm);
    if (evaluationPrevue.id !== null) {
      this.subscribeToSaveResponse(this.evaluationPrevueService.update(evaluationPrevue));
    } else {
      this.subscribeToSaveResponse(this.evaluationPrevueService.create(evaluationPrevue));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEvaluationPrevue>>): void {
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

  protected updateForm(evaluationPrevue: IEvaluationPrevue): void {
    this.evaluationPrevue = evaluationPrevue;
    this.evaluationPrevueFormService.resetForm(this.editForm, evaluationPrevue);

    this.cyclesSharedCollection = this.cycleService.addCycleToCollectionIfMissing<ICycle>(
      this.cyclesSharedCollection,
      evaluationPrevue.cycle,
    );
    this.enseignantsSharedCollection = this.enseignantService.addEnseignantToCollectionIfMissing<IEnseignant>(
      this.enseignantsSharedCollection,
      evaluationPrevue.enseignant,
    );
    this.matieresSharedCollection = this.matiereService.addMatiereToCollectionIfMissing<IMatiere>(
      this.matieresSharedCollection,
      evaluationPrevue.matiere,
    );
    this.sousMatieresSharedCollection = this.sousMatiereService.addSousMatiereToCollectionIfMissing<ISousMatiere>(
      this.sousMatieresSharedCollection,
      evaluationPrevue.sousMatiere,
    );
    this.coursSharedCollection = this.coursService.addCoursToCollectionIfMissing<ICours>(
      this.coursSharedCollection,
      evaluationPrevue.cours,
    );
    this.typeTachesSharedCollection = this.typeTacheService.addTypeTacheToCollectionIfMissing<ITypeTache>(
      this.typeTachesSharedCollection,
      evaluationPrevue.typeTache,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.cycleService
      .query()
      .pipe(map((res: HttpResponse<ICycle[]>) => res.body ?? []))
      .pipe(map((cycles: ICycle[]) => this.cycleService.addCycleToCollectionIfMissing<ICycle>(cycles, this.evaluationPrevue?.cycle)))
      .subscribe((cycles: ICycle[]) => (this.cyclesSharedCollection = cycles));

    this.enseignantService
      .query()
      .pipe(map((res: HttpResponse<IEnseignant[]>) => res.body ?? []))
      .pipe(
        map((enseignants: IEnseignant[]) =>
          this.enseignantService.addEnseignantToCollectionIfMissing<IEnseignant>(enseignants, this.evaluationPrevue?.enseignant),
        ),
      )
      .subscribe((enseignants: IEnseignant[]) => (this.enseignantsSharedCollection = enseignants));

    this.matiereService
      .query()
      .pipe(map((res: HttpResponse<IMatiere[]>) => res.body ?? []))
      .pipe(
        map((matieres: IMatiere[]) =>
          this.matiereService.addMatiereToCollectionIfMissing<IMatiere>(matieres, this.evaluationPrevue?.matiere),
        ),
      )
      .subscribe((matieres: IMatiere[]) => (this.matieresSharedCollection = matieres));

    this.sousMatiereService
      .query()
      .pipe(map((res: HttpResponse<ISousMatiere[]>) => res.body ?? []))
      .pipe(
        map((sousMatieres: ISousMatiere[]) =>
          this.sousMatiereService.addSousMatiereToCollectionIfMissing<ISousMatiere>(sousMatieres, this.evaluationPrevue?.sousMatiere),
        ),
      )
      .subscribe((sousMatieres: ISousMatiere[]) => (this.sousMatieresSharedCollection = sousMatieres));

    this.coursService
      .query()
      .pipe(map((res: HttpResponse<ICours[]>) => res.body ?? []))
      .pipe(map((cours: ICours[]) => this.coursService.addCoursToCollectionIfMissing<ICours>(cours, this.evaluationPrevue?.cours)))
      .subscribe((cours: ICours[]) => (this.coursSharedCollection = cours));

    this.typeTacheService
      .query()
      .pipe(map((res: HttpResponse<ITypeTache[]>) => res.body ?? []))
      .pipe(
        map((typeTaches: ITypeTache[]) =>
          this.typeTacheService.addTypeTacheToCollectionIfMissing<ITypeTache>(typeTaches, this.evaluationPrevue?.typeTache),
        ),
      )
      .subscribe((typeTaches: ITypeTache[]) => (this.typeTachesSharedCollection = typeTaches));
  }
}
