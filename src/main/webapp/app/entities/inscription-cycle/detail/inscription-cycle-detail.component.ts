import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatePipe } from 'app/shared/date';
import { IInscriptionCycle } from '../inscription-cycle.model';

@Component({
  selector: 'jhi-inscription-cycle-detail',
  templateUrl: './inscription-cycle-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatePipe],
})
export class InscriptionCycleDetailComponent {
  inscriptionCycle = input<IInscriptionCycle | null>(null);

  previousState(): void {
    window.history.back();
  }
}
