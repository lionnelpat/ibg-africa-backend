import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatePipe } from 'app/shared/date';
import { IEvaluationPrevue } from '../evaluation-prevue.model';

@Component({
  selector: 'jhi-evaluation-prevue-detail',
  templateUrl: './evaluation-prevue-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatePipe],
})
export class EvaluationPrevueDetailComponent {
  evaluationPrevue = input<IEvaluationPrevue | null>(null);

  previousState(): void {
    window.history.back();
  }
}
