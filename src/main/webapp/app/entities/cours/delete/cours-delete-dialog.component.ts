import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICours } from '../cours.model';
import { CoursService } from '../service/cours.service';

@Component({
  templateUrl: './cours-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CoursDeleteDialogComponent {
  cours?: ICours;

  protected coursService = inject(CoursService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.coursService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
