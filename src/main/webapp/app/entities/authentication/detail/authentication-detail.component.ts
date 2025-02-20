import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IAuthentication } from '../authentication.model';

@Component({
  standalone: true,
  selector: 'jhi-authentication-detail',
  templateUrl: './authentication-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class AuthenticationDetailComponent {
  authentication = input<IAuthentication | null>(null);

  previousState(): void {
    window.history.back();
  }
}
