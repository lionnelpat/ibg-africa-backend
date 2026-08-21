import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IEvaluationPrevue } from '../evaluation-prevue.model';
import { EvaluationPrevueService } from '../service/evaluation-prevue.service';

@Component({
  templateUrl: './evaluation-prevue-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class EvaluationPrevueDeleteDialogComponent {
  evaluationPrevue?: IEvaluationPrevue;

  protected evaluationPrevueService = inject(EvaluationPrevueService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.evaluationPrevueService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
