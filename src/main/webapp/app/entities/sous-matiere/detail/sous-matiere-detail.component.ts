import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { ISousMatiere } from '../sous-matiere.model';

@Component({
  selector: 'jhi-sous-matiere-detail',
  templateUrl: './sous-matiere-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class SousMatiereDetailComponent {
  sousMatiere = input<ISousMatiere | null>(null);

  previousState(): void {
    window.history.back();
  }
}
