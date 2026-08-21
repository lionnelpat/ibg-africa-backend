import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IEtudiant } from 'app/entities/etudiant/etudiant.model';
import { EtudiantService } from 'app/entities/etudiant/service/etudiant.service';
import { IEvenementEtudiant } from '../evenement-etudiant.model';
import { EvenementEtudiantService } from '../service/evenement-etudiant.service';
import { EvenementEtudiantFormGroup, EvenementEtudiantFormService } from './evenement-etudiant-form.service';

@Component({
  selector: 'jhi-evenement-etudiant-update',
  templateUrl: './evenement-etudiant-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class EvenementEtudiantUpdateComponent implements OnInit {
  isSaving = false;
  evenementEtudiant: IEvenementEtudiant | null = null;

  etudiantsSharedCollection: IEtudiant[] = [];

  protected evenementEtudiantService = inject(EvenementEtudiantService);
  protected evenementEtudiantFormService = inject(EvenementEtudiantFormService);
  protected etudiantService = inject(EtudiantService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: EvenementEtudiantFormGroup = this.evenementEtudiantFormService.createEvenementEtudiantFormGroup();

  compareEtudiant = (o1: IEtudiant | null, o2: IEtudiant | null): boolean => this.etudiantService.compareEtudiant(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ evenementEtudiant }) => {
      this.evenementEtudiant = evenementEtudiant;
      if (evenementEtudiant) {
        this.updateForm(evenementEtudiant);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const evenementEtudiant = this.evenementEtudiantFormService.getEvenementEtudiant(this.editForm);
    if (evenementEtudiant.id !== null) {
      this.subscribeToSaveResponse(this.evenementEtudiantService.update(evenementEtudiant));
    } else {
      this.subscribeToSaveResponse(this.evenementEtudiantService.create(evenementEtudiant));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEvenementEtudiant>>): void {
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

  protected updateForm(evenementEtudiant: IEvenementEtudiant): void {
    this.evenementEtudiant = evenementEtudiant;
    this.evenementEtudiantFormService.resetForm(this.editForm, evenementEtudiant);

    this.etudiantsSharedCollection = this.etudiantService.addEtudiantToCollectionIfMissing<IEtudiant>(
      this.etudiantsSharedCollection,
      evenementEtudiant.etudiant,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.etudiantService
      .query()
      .pipe(map((res: HttpResponse<IEtudiant[]>) => res.body ?? []))
      .pipe(
        map((etudiants: IEtudiant[]) =>
          this.etudiantService.addEtudiantToCollectionIfMissing<IEtudiant>(etudiants, this.evenementEtudiant?.etudiant),
        ),
      )
      .subscribe((etudiants: IEtudiant[]) => (this.etudiantsSharedCollection = etudiants));
  }
}
