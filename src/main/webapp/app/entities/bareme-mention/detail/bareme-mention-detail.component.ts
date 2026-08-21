import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { IBaremeMention } from '../bareme-mention.model';

@Component({
  selector: 'jhi-bareme-mention-detail',
  templateUrl: './bareme-mention-detail.component.html',
  imports: [SharedModule, RouterModule],
})
export class BaremeMentionDetailComponent {
  baremeMention = input<IBaremeMention | null>(null);

  previousState(): void {
    window.history.back();
  }
}
