import { Component, NgZone, OnInit, inject } from '@angular/core';
import { ActivatedRoute, Data, ParamMap, Router, RouterModule } from '@angular/router';
import { Observable, Subscription, combineLatest, filter, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { SortByDirective, SortDirective, SortService, type SortState, sortStateSignal } from 'app/shared/sort';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { FormsModule } from '@angular/forms';
import { DEFAULT_SORT_DATA, ITEM_DELETED_EVENT, SORT } from 'app/config/navigation.constants';
import { IConversation } from '../conversation.model';
import { ConversationService, EntityArrayResponseType } from '../service/conversation.service';
import { ConversationDeleteDialogComponent } from '../delete/conversation-delete-dialog.component';
import { AccountService } from 'app/core/auth/account.service';
import { ProfileDetailsService } from 'app/entities/profile-details/service/profile-details.service';

@Component({
  standalone: true,
  selector: 'jhi-conversation',
  templateUrl: './conversation.component.html',
  styleUrls: ['./conversation.component.css'],
  imports: [
    RouterModule,
    FormsModule,
    SharedModule,
    SortDirective,
    SortByDirective,
    DurationPipe,
    FormatMediumDatetimePipe,
    FormatMediumDatePipe,
  ],
})
export class ConversationComponent implements OnInit {
  subscription: Subscription | null = null;
  conversations?: IConversation[];
  isLoading = false;
  isAdmin = false;

  sortState = sortStateSignal({});

  viewerProfileId: number | null = null;

  public readonly router = inject(Router);
  protected readonly conversationService = inject(ConversationService);
  protected readonly activatedRoute = inject(ActivatedRoute);
  protected readonly sortService = inject(SortService);
  protected modalService = inject(NgbModal);
  protected ngZone = inject(NgZone);
  protected accountService = inject(AccountService);
  protected profileService = inject(ProfileDetailsService);

  private nameCache = new Map<number, string>();

  trackId = (item: IConversation): number => this.conversationService.getConversationIdentifier(item);

  ngOnInit(): void {
    this.subscription = combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data])
      .pipe(
        tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
        tap(() => {
          this.accountService.identity().subscribe(acc => {
            const roles = acc?.authorities ?? [];
            this.isAdmin = roles.includes('ROLE_ADMIN');

            if (!this.conversations || this.conversations.length === 0) {
              if (this.isAdmin) {
                this.loadAll();
              } else {
                this.loadVibes();
              }
            }
          });
        }),
      )
      .subscribe();

    this.profileService.getCurrentUserProfile().subscribe(profile => {
      this.viewerProfileId = profile.id;
      if (this.conversations) {
        this.conversations = [...this.conversations];
      }
    });
  }

  delete(conversation: IConversation): void {
    const modalRef = this.modalService.open(ConversationDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.conversation = conversation;
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        tap(() => {
          if (this.isAdmin) {
            this.loadAll();
          } else {
            this.loadVibes();
          }
        }),
      )
      .subscribe();
  }

  loadVibes(): void {
    this.grabVibesBackend().subscribe({
      next: (res: EntityArrayResponseType) => {
        this.onResponseSuccess(res);
      },
    });
  }

  loadAll(): void {
    this.queryBackend().subscribe({
      next: res => this.onResponseSuccess(res),
    });
  }

  navigateToWithComponentValues(event: SortState): void {
    this.handleNavigation(event);
  }

  getConversationTitle(conv: IConversation): string {
    const profiles = [...(conv.profileDetails ?? []), ...(conv.participants ?? [])];

    if (profiles.length === 0) {
      return `Conversation #${conv.id}`;
    }

    if (this.isAdmin) {
      return this.getParticipantsNames(profiles).join(' & ');
    }

    const other = profiles.find(p => p.id !== this.viewerProfileId);
    return other ? this.formatName(other.id) : `Conversation #${conv.id}`;
  }

  getAdminSubtitle(conv: IConversation): string {
    const profiles = [...(conv.profileDetails ?? []), ...(conv.participants ?? [])];
    return this.getParticipantsNames(profiles).join(' & ');
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    this.sortState.set(this.sortService.parseSortParam(params.get(SORT) ?? data[DEFAULT_SORT_DATA]));
  }

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.conversations = this.refineData(dataFromBody);

    this.conversations.forEach(conv => {
      const ids = new Set<number>([...(conv.profileDetails?.map(p => p.id) ?? []), ...(conv.participants?.map(p => p.id) ?? [])]);
      ids.forEach(id => this.ensureNameCached(id));
    });
  }

  protected refineData(data: IConversation[]): IConversation[] {
    const { predicate, order } = this.sortState();
    return predicate && order ? data.sort(this.sortService.startSort({ predicate, order })) : data;
  }

  protected fillComponentAttributesFromResponseBody(data: IConversation[] | null): IConversation[] {
    return data ?? [];
  }

  protected grabVibesBackend(): Observable<EntityArrayResponseType> {
    this.isLoading = true;
    return this.conversationService.fetchMyVibes().pipe(tap(() => (this.isLoading = false)));
  }

  protected queryBackend(): Observable<EntityArrayResponseType> {
    this.isLoading = true;
    return this.conversationService
      .query({ eagerload: true, sort: this.sortService.buildSortParam(this.sortState()) })
      .pipe(tap(() => (this.isLoading = false)));
  }

  protected handleNavigation(sortState: SortState): void {
    const queryParamsObj = {
      sort: this.sortService.buildSortParam(sortState),
    };

    this.ngZone.run(() => {
      this.router.navigate(['./'], {
        relativeTo: this.activatedRoute,
        queryParams: queryParamsObj,
      });
    });
  }

  private getParticipantsNames(arr: { id: number }[] | null | undefined): string[] {
    return arr ? arr.map(p => this.formatName(p.id)) : [];
  }

  private formatName(profileId: number): string {
    return this.nameCache.get(profileId) ?? `User ${profileId}`;
  }

  private ensureNameCached(id: number): void {
    if (this.nameCache.has(id)) return;

    this.profileService.find(id).subscribe(res => {
      const pd = res.body;
      const computedName = pd?.userName ?? pd?.user?.login ?? `User ${id}`;
      this.nameCache.set(id, computedName);

      if (this.conversations) {
        this.conversations = [...this.conversations];
      }
    });
  }
}
