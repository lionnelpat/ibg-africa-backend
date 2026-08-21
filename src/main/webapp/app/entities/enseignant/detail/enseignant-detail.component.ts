import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IEnseignant } from '../enseignant.model';

@Component({
  selector: 'jhi-enseignant-detail',
  templateUrl: './enseignant-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class EnseignantDetailComponent {
  enseignant = input<IEnseignant | null>(null);

  previousState(): void {
    window.history.back();
  }
}
