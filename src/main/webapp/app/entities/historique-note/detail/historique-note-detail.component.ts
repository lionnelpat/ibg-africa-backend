import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { FormatMediumDatetimePipe } from 'app/shared/date';
import { IHistoriqueNote } from '../historique-note.model';

@Component({
  selector: 'jhi-historique-note-detail',
  templateUrl: './historique-note-detail.component.html',
  imports: [SharedModule, RouterModule, FormatMediumDatetimePipe],
})
export class HistoriqueNoteDetailComponent {
  historiqueNote = input<IHistoriqueNote | null>(null);

  previousState(): void {
    window.history.back();
  }
}
