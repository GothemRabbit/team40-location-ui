import { Component, inject, Input, input, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { DataUtils } from 'app/core/util/data-util.service';
import { IProfileDetails } from '../profile-details.model';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import reviewRoute from '../../review/review.routes';

@Component({
  standalone: true,
  selector: 'jhi-profile-details-detail',
  templateUrl: './profile-details-detail.component.html',
  styleUrl: './profile-details-detail.component.scss',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ProfileDetailsDetailComponent implements OnInit {
  @Input() profileDetails: IProfileDetails | null = null;
  activeTab = 'listings';
  isOwner = false;
  account: Account | null = null;

  protected dataUtils = inject(DataUtils);
  protected accountService = inject(AccountService);

  ngOnInit(): void {
    this.accountService.identity().subscribe(account => {
      this.account = account;
      this.checkOwnership();
    });
  }

  checkOwnership(): void {
    const profile = this.profileDetails;
    // eslint-disable-next-line no-console
    console.log(profile?.userName);
    // eslint-disable-next-line no-console
    console.log(this.account?.login);
    this.isOwner = this.account?.login === profile?.userName;
    // eslint-disable-next-line no-console
    console.log(this.isOwner);
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
