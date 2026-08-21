import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICycle } from '../cycle.model';
import { CycleService } from '../service/cycle.service';

@Component({
  templateUrl: './cycle-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CycleDeleteDialogComponent {
  cycle?: ICycle;

  protected cycleService = inject(CycleService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cycleService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
