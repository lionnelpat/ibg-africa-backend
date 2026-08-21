import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ITypeTache } from '../type-tache.model';
import { TypeTacheService } from '../service/type-tache.service';
import { TypeTacheFormGroup, TypeTacheFormService } from './type-tache-form.service';

@Component({
  selector: 'jhi-type-tache-update',
  templateUrl: './type-tache-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TypeTacheUpdateComponent implements OnInit {
  isSaving = false;
  typeTache: ITypeTache | null = null;

  protected typeTacheService = inject(TypeTacheService);
  protected typeTacheFormService = inject(TypeTacheFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: TypeTacheFormGroup = this.typeTacheFormService.createTypeTacheFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ typeTache }) => {
      this.typeTache = typeTache;
      if (typeTache) {
        this.updateForm(typeTache);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const typeTache = this.typeTacheFormService.getTypeTache(this.editForm);
    if (typeTache.id !== null) {
      this.subscribeToSaveResponse(this.typeTacheService.update(typeTache));
    } else {
      this.subscribeToSaveResponse(this.typeTacheService.create(typeTache));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITypeTache>>): void {
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

  protected updateForm(typeTache: ITypeTache): void {
    this.typeTache = typeTache;
    this.typeTacheFormService.resetForm(this.editForm, typeTache);
  }
}
