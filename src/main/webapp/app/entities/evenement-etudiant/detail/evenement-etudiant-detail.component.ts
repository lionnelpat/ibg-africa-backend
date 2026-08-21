import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatePipe } from 'app/shared/date';
import { IEvenementEtudiant } from '../evenement-etudiant.model';

@Component({
  selector: 'jhi-evenement-etudiant-detail',
  templateUrl: './evenement-etudiant-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatePipe],
})
export class EvenementEtudiantDetailComponent {
  evenementEtudiant = input<IEvenementEtudiant | null>(null);

  previousState(): void {
    window.history.back();
  }
}
