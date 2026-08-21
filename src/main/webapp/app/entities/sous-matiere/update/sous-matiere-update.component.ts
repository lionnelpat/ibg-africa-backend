import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ISousMatiere } from '../sous-matiere.model';
import { SousMatiereService } from '../service/sous-matiere.service';
import { SousMatiereFormGroup, SousMatiereFormService } from './sous-matiere-form.service';

@Component({
  selector: 'jhi-sous-matiere-update',
  templateUrl: './sous-matiere-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SousMatiereUpdateComponent implements OnInit {
  isSaving = false;
  sousMatiere: ISousMatiere | null = null;

  protected sousMatiereService = inject(SousMatiereService);
  protected sousMatiereFormService = inject(SousMatiereFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SousMatiereFormGroup = this.sousMatiereFormService.createSousMatiereFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sousMatiere }) => {
      this.sousMatiere = sousMatiere;
      if (sousMatiere) {
        this.updateForm(sousMatiere);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sousMatiere = this.sousMatiereFormService.getSousMatiere(this.editForm);
    if (sousMatiere.id !== null) {
      this.subscribeToSaveResponse(this.sousMatiereService.update(sousMatiere));
    } else {
      this.subscribeToSaveResponse(this.sousMatiereService.create(sousMatiere));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISousMatiere>>): void {
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

  protected updateForm(sousMatiere: ISousMatiere): void {
    this.sousMatiere = sousMatiere;
    this.sousMatiereFormService.resetForm(this.editForm, sousMatiere);
  }
}
