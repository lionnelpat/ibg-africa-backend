import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IEvaluationRealisee } from '../evaluation-realisee.model';

@Component({
  selector: 'jhi-evaluation-realisee-detail',
  templateUrl: './evaluation-realisee-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class EvaluationRealiseeDetailComponent {
  evaluationRealisee = input<IEvaluationRealisee | null>(null);

  previousState(): void {
    window.history.back();
  }
}
