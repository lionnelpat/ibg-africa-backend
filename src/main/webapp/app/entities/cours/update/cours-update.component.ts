import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICours } from '../cours.model';
import { CoursService } from '../service/cours.service';
import { CoursFormGroup, CoursFormService } from './cours-form.service';

@Component({
  selector: 'jhi-cours-update',
  templateUrl: './cours-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CoursUpdateComponent implements OnInit {
  isSaving = false;
  cours: ICours | null = null;

  protected coursService = inject(CoursService);
  protected coursFormService = inject(CoursFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CoursFormGroup = this.coursFormService.createCoursFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cours }) => {
      this.cours = cours;
      if (cours) {
        this.updateForm(cours);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cours = this.coursFormService.getCours(this.editForm);
    if (cours.id !== null) {
      this.subscribeToSaveResponse(this.coursService.update(cours));
    } else {
      this.subscribeToSaveResponse(this.coursService.create(cours));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICours>>): void {
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

  protected updateForm(cours: ICours): void {
    this.cours = cours;
    this.coursFormService.resetForm(this.editForm, cours);
  }
}
