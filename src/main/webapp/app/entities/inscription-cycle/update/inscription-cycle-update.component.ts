import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICycle } from 'app/entities/cycle/cycle.model';
import { CycleService } from 'app/entities/cycle/service/cycle.service';
import { IEtudiant } from 'app/entities/etudiant/etudiant.model';
import { EtudiantService } from 'app/entities/etudiant/service/etudiant.service';
import { InscriptionCycleService } from '../service/inscription-cycle.service';
import { IInscriptionCycle } from '../inscription-cycle.model';
import { InscriptionCycleFormGroup, InscriptionCycleFormService } from './inscription-cycle-form.service';

@Component({
  selector: 'jhi-inscription-cycle-update',
  templateUrl: './inscription-cycle-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class InscriptionCycleUpdateComponent implements OnInit {
  isSaving = false;
  inscriptionCycle: IInscriptionCycle | null = null;

  cyclesSharedCollection: ICycle[] = [];
  etudiantsSharedCollection: IEtudiant[] = [];

  protected inscriptionCycleService = inject(InscriptionCycleService);
  protected inscriptionCycleFormService = inject(InscriptionCycleFormService);
  protected cycleService = inject(CycleService);
  protected etudiantService = inject(EtudiantService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: InscriptionCycleFormGroup = this.inscriptionCycleFormService.createInscriptionCycleFormGroup();

  compareCycle = (o1: ICycle | null, o2: ICycle | null): boolean => this.cycleService.compareCycle(o1, o2);

  compareEtudiant = (o1: IEtudiant | null, o2: IEtudiant | null): boolean => this.etudiantService.compareEtudiant(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ inscriptionCycle }) => {
      this.inscriptionCycle = inscriptionCycle;
      if (inscriptionCycle) {
        this.updateForm(inscriptionCycle);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const inscriptionCycle = this.inscriptionCycleFormService.getInscriptionCycle(this.editForm);
    if (inscriptionCycle.id !== null) {
      this.subscribeToSaveResponse(this.inscriptionCycleService.update(inscriptionCycle));
    } else {
      this.subscribeToSaveResponse(this.inscriptionCycleService.create(inscriptionCycle));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInscriptionCycle>>): void {
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

  protected updateForm(inscriptionCycle: IInscriptionCycle): void {
    this.inscriptionCycle = inscriptionCycle;
    this.inscriptionCycleFormService.resetForm(this.editForm, inscriptionCycle);

    this.cyclesSharedCollection = this.cycleService.addCycleToCollectionIfMissing<ICycle>(
      this.cyclesSharedCollection,
      inscriptionCycle.cycle,
    );
    this.etudiantsSharedCollection = this.etudiantService.addEtudiantToCollectionIfMissing<IEtudiant>(
      this.etudiantsSharedCollection,
      inscriptionCycle.etudiant,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.cycleService
      .query()
      .pipe(map((res: HttpResponse<ICycle[]>) => res.body ?? []))
      .pipe(map((cycles: ICycle[]) => this.cycleService.addCycleToCollectionIfMissing<ICycle>(cycles, this.inscriptionCycle?.cycle)))
      .subscribe((cycles: ICycle[]) => (this.cyclesSharedCollection = cycles));

    this.etudiantService
      .query()
      .pipe(map((res: HttpResponse<IEtudiant[]>) => res.body ?? []))
      .pipe(
        map((etudiants: IEtudiant[]) =>
          this.etudiantService.addEtudiantToCollectionIfMissing<IEtudiant>(etudiants, this.inscriptionCycle?.etudiant),
        ),
      )
      .subscribe((etudiants: IEtudiant[]) => (this.etudiantsSharedCollection = etudiants));
  }
}
