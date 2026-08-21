import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IEnseignant } from '../enseignant.model';
import { EnseignantService } from '../service/enseignant.service';
import { EnseignantFormGroup, EnseignantFormService } from './enseignant-form.service';

@Component({
  selector: 'jhi-enseignant-update',
  templateUrl: './enseignant-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class EnseignantUpdateComponent implements OnInit {
  isSaving = false;
  enseignant: IEnseignant | null = null;

  protected enseignantService = inject(EnseignantService);
  protected enseignantFormService = inject(EnseignantFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: EnseignantFormGroup = this.enseignantFormService.createEnseignantFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ enseignant }) => {
      this.enseignant = enseignant;
      if (enseignant) {
        this.updateForm(enseignant);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const enseignant = this.enseignantFormService.getEnseignant(this.editForm);
    if (enseignant.id !== null) {
      this.subscribeToSaveResponse(this.enseignantService.update(enseignant));
    } else {
      this.subscribeToSaveResponse(this.enseignantService.create(enseignant));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEnseignant>>): void {
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

  protected updateForm(enseignant: IEnseignant): void {
    this.enseignant = enseignant;
    this.enseignantFormService.resetForm(this.editForm, enseignant);
  }
}
