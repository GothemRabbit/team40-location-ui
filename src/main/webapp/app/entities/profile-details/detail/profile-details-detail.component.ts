import { Component, inject, Input, input, OnInit } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { DataUtils } from 'app/core/util/data-util.service';
import { IProfileDetails } from '../profile-details.model';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { ItemComponent } from '../../item/list/item.component';
import { ReviewComponent } from '../../review/list/review.component';
import { IReview } from '../../review/review.model';
import { ReviewService } from '../../review/service/review.service';
import { faArrowUp, faArrowDown } from '@fortawesome/free-solid-svg-icons';
import { ItemService } from '../../item/service/item.service';
import { IItem } from '../../item/item.model';

@Component({
  standalone: true,
  selector: 'jhi-profile-details-detail',
  templateUrl: './profile-details-detail.component.html',
  styleUrl: './profile-details-detail.component.scss',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe, ItemComponent, ReviewComponent],
})
export class ProfileDetailsDetailComponent implements OnInit {
  @Input() profileDetails: IProfileDetails | null = null;
  activeTab = 'listings';
  isOwner = false;
  account: Account | null = null;
  reviewReceived?: IReview[];
  sort?: boolean = false;
  items: IItem[] = [];

  protected dataUtils = inject(DataUtils);
  protected accountService = inject(AccountService);
  protected reviewService = inject(ReviewService);
  protected readonly faArrowUp = faArrowUp;
  protected readonly faArrowDown = faArrowDown;
  private activatedRoute = inject(ActivatedRoute);
  private itemService = inject(ItemService);

  trackId = (item: IReview): number => this.reviewService.getReviewIdentifier(item);
  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ profileDetails }) => {
      this.profileDetails = profileDetails;
      this.reviewService.getReviewsAboutUser(profileDetails.id).subscribe(reviews => {
        this.reviewReceived = reviews;
      });
      if (profileDetails.id) {
        this.itemService.getItemsByProfile(profileDetails.id).subscribe(res => {
          this.items = res.body ?? [];
        });
      }
      this.accountService.identity().subscribe(account => {
        this.account = account;
        this.checkOwnership();
      });
    });
  }

  checkOwnership(): void {
    const profile = this.profileDetails;
    // eslint-disable-next-line no-console
    console.log('profile ' + String(profile?.userName));
    // eslint-disable-next-line no-console
    console.log('login: ' + String(this.account?.login));
    this.isOwner = this.account?.login === profile?.userName;
    // eslint-disable-next-line no-console
    console.log('isOwner: ' + String(this.isOwner));
  }

  getAverageRating(): number {
    if (this.reviewReceived == null || this.reviewReceived.length === 0) {
      return 0;
    }
    const average: number = this.reviewReceived.reduce((total, r) => Number(r.rating) + total, 0);
    return Number((average / this.reviewReceived.length).toFixed(2));
  }

  public sortReviewASC(): void {
    this.reviewReceived = this.reviewReceived?.sort(
      (a, b) => new Date(b.date?.date() ?? 0).getTime() - new Date(a.date?.date() ?? 0).getTime(),
    );
  }
  public sortReviewDSC(): void {
    this.reviewReceived = this.reviewReceived?.sort(
      (a, b) => new Date(a.date?.date() ?? 0).getTime() - new Date(b.date?.date() ?? 0).getTime(),
    );
  }
  public sortButton(): void {
    if (!this.sort) {
      this.sortReviewDSC();
    } else {
      this.sortReviewASC();
    }
    this.sort = !this.sort;
  }

  filterBy(Inputs: HTMLSelectElement): void {
    if (Number(Inputs.value)) {
      this.reviewReceived = this.reviewReceived?.filter(r => Number(r.rating) === Number(Inputs.value));
    }
  }

  likeButton(review: IReview): void {
    review.liked = !review.liked;
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
