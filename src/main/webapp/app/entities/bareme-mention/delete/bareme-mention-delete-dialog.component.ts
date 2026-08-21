import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IBaremeMention } from '../bareme-mention.model';
import { BaremeMentionService } from '../service/bareme-mention.service';

@Component({
  templateUrl: './bareme-mention-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class BaremeMentionDeleteDialogComponent {
  baremeMention?: IBaremeMention;

  protected baremeMentionService = inject(BaremeMentionService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.baremeMentionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
