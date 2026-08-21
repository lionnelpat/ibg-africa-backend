import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatePipe } from 'app/shared/date';
import { ICours } from '../cours.model';

@Component({
  selector: 'jhi-cours-detail',
  templateUrl: './cours-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatePipe],
})
export class CoursDetailComponent {
  cours = input<ICours | null>(null);

  previousState(): void {
    window.history.back();
  }
}
