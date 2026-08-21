import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IPays } from '../pays.model';
import { PaysService } from '../service/pays.service';

@Component({
  templateUrl: './pays-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class PaysDeleteDialogComponent {
  pays?: IPays;

  protected paysService = inject(PaysService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.paysService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
