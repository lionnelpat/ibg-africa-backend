import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { ITypeTache } from '../type-tache.model';

@Component({
  selector: 'jhi-type-tache-detail',
  templateUrl: './type-tache-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class TypeTacheDetailComponent {
  typeTache = input<ITypeTache | null>(null);

  previousState(): void {
    window.history.back();
  }
}
