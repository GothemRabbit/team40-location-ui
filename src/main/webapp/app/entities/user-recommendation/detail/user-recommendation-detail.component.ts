import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IUserRecommendation } from '../user-recommendation.model';

@Component({
  standalone: true,
  selector: 'jhi-user-recommendation-detail',
  templateUrl: './user-recommendation-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class UserRecommendationDetailComponent {
  userRecommendation = input<IUserRecommendation | null>(null);

  previousState(): void {
    window.history.back();
  }
}
