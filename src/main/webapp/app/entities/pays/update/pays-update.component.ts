import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPays } from '../pays.model';
import { PaysService } from '../service/pays.service';
import { PaysFormGroup, PaysFormService } from './pays-form.service';

@Component({
  selector: 'jhi-pays-update',
  templateUrl: './pays-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PaysUpdateComponent implements OnInit {
  isSaving = false;
  pays: IPays | null = null;

  protected paysService = inject(PaysService);
  protected paysFormService = inject(PaysFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PaysFormGroup = this.paysFormService.createPaysFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pays }) => {
      this.pays = pays;
      if (pays) {
        this.updateForm(pays);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pays = this.paysFormService.getPays(this.editForm);
    if (pays.id !== null) {
      this.subscribeToSaveResponse(this.paysService.update(pays));
    } else {
      this.subscribeToSaveResponse(this.paysService.create(pays));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPays>>): void {
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

  protected updateForm(pays: IPays): void {
    this.pays = pays;
    this.paysFormService.resetForm(this.editForm, pays);
  }
}
