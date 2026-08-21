import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatePipe } from 'app/shared/date';
import { ICycle } from '../cycle.model';

@Component({
  selector: 'jhi-cycle-detail',
  templateUrl: './cycle-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatePipe],
})
export class CycleDetailComponent {
  cycle = input<ICycle | null>(null);

  previousState(): void {
    window.history.back();
  }
}
