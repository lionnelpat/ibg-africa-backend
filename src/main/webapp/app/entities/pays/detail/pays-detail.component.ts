import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IPays } from '../pays.model';

@Component({
  selector: 'jhi-pays-detail',
  templateUrl: './pays-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class PaysDetailComponent {
  pays = input<IPays | null>(null);

  previousState(): void {
    window.history.back();
  }
}
