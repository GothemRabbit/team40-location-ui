import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IUserRecommendation } from '../user-recommendation.model';
import { UserRecommendationService } from '../service/user-recommendation.service';

@Component({
  standalone: true,
  templateUrl: './user-recommendation-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class UserRecommendationDeleteDialogComponent {
  userRecommendation?: IUserRecommendation;

  protected userRecommendationService = inject(UserRecommendationService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.userRecommendationService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
