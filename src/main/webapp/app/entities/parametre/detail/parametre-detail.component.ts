import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IParametre } from '../parametre.model';

@Component({
  selector: 'jhi-parametre-detail',
  templateUrl: './parametre-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class ParametreDetailComponent {
  parametre = input<IParametre | null>(null);

  previousState(): void {
    window.history.back();
  }
}
