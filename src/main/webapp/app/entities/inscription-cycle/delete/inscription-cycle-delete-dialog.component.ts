import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IInscriptionCycle } from '../inscription-cycle.model';
import { InscriptionCycleService } from '../service/inscription-cycle.service';

@Component({
  templateUrl: './inscription-cycle-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class InscriptionCycleDeleteDialogComponent {
  inscriptionCycle?: IInscriptionCycle;

  protected inscriptionCycleService = inject(InscriptionCycleService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.inscriptionCycleService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
