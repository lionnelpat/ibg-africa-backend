import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatePipe } from 'app/shared/date';
import { IHabilitationCycle } from '../habilitation-cycle.model';

@Component({
  selector: 'jhi-habilitation-cycle-detail',
  templateUrl: './habilitation-cycle-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatePipe],
})
export class HabilitationCycleDetailComponent {
  habilitationCycle = input<IHabilitationCycle | null>(null);

  previousState(): void {
    window.history.back();
  }
}
