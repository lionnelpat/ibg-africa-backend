import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICentreFormation } from '../centre-formation.model';
import { CentreFormationService } from '../service/centre-formation.service';

@Component({
  templateUrl: './centre-formation-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CentreFormationDeleteDialogComponent {
  centreFormation?: ICentreFormation;

  protected centreFormationService = inject(CentreFormationService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.centreFormationService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
