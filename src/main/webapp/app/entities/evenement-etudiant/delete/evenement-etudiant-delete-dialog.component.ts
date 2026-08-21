import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IEvenementEtudiant } from '../evenement-etudiant.model';
import { EvenementEtudiantService } from '../service/evenement-etudiant.service';

@Component({
  templateUrl: './evenement-etudiant-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class EvenementEtudiantDeleteDialogComponent {
  evenementEtudiant?: IEvenementEtudiant;

  protected evenementEtudiantService = inject(EvenementEtudiantService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.evenementEtudiantService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
