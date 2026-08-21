import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IEvaluationRealisee } from '../evaluation-realisee.model';
import { EvaluationRealiseeService } from '../service/evaluation-realisee.service';

@Component({
  templateUrl: './evaluation-realisee-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class EvaluationRealiseeDeleteDialogComponent {
  evaluationRealisee?: IEvaluationRealisee;

  protected evaluationRealiseeService = inject(EvaluationRealiseeService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.evaluationRealiseeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
