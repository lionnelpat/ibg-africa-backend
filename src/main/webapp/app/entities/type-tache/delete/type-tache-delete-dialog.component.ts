import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITypeTache } from '../type-tache.model';
import { TypeTacheService } from '../service/type-tache.service';

@Component({
  templateUrl: './type-tache-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TypeTacheDeleteDialogComponent {
  typeTache?: ITypeTache;

  protected typeTacheService = inject(TypeTacheService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.typeTacheService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
