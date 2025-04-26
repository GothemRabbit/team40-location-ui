import { Component, NgZone, OnInit, inject } from '@angular/core';
import { ActivatedRoute, Data, ParamMap, Router, RouterModule } from '@angular/router';
import { Observable, Subscription, combineLatest, filter, tap, map } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { HttpClient } from '@angular/common/http';

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

interface ChatSnippet {
  content?: string;
  profileDetails?: { id: number };
}

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
  protected http = inject(HttpClient);

  private tagBank = new Map<number, string>();
  private buzzCache = new Map<number, string>();

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
        this.fetchBuzz();
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

  getConvoHeadline(conv: IConversation): string {
    const profiles = [...(conv.profileDetails ?? []), ...(conv.participants ?? [])];

    if (profiles.length === 0) {
      return `Conversation #${conv.id}`;
    }

    if (this.isAdmin) {
      return this.getCrewTags(profiles).join(' & ');
    }

    const other = profiles.find(p => p.id !== this.viewerProfileId);
    return other ? this.vibeTag(other.id) : `Conversation #${conv.id}`;
  }

  getAdminSubtext(conv: IConversation): string {
    const profiles = [...(conv.profileDetails ?? []), ...(conv.participants ?? [])];
    return this.getCrewTags(profiles).join(' & ');
  }

  getBuzzPreview(conv: IConversation): string | null {
    return this.buzzCache.get(conv.id) ?? null;
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    this.sortState.set(this.sortService.parseSortParam(params.get(SORT) ?? data[DEFAULT_SORT_DATA]));
  }

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.conversations = this.refineData(dataFromBody);

    this.conversations.forEach(conv => {
      const ids = new Set<number>([...(conv.profileDetails?.map(p => p.id) ?? []), ...(conv.participants?.map(p => p.id) ?? [])]);
      ids.forEach(id => this.ensureTagCached(id));
    });

    this.fetchBuzz();
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

  private getCrewTags(arr: { id: number }[] | null | undefined): string[] {
    return arr ? arr.map(p => this.vibeTag(p.id)) : [];
  }

  private vibeTag(profileId: number): string {
    return this.tagBank.get(profileId) ?? `User ${profileId}`;
  }

  private ensureTagCached(id: number): void {
    if (this.tagBank.has(id)) return;

    this.profileService.find(id).subscribe(res => {
      const pd = res.body;
      const computedName = pd?.userName ?? pd?.user?.login ?? `User ${id}`;
      this.tagBank.set(id, computedName);

      if (this.conversations) {
        this.conversations = [...this.conversations];
      }
    });
  }

  private fetchBuzz(): void {
    if (!this.conversations) return;

    this.conversations.forEach(conv => {
      if (!this.buzzCache.has(conv.id)) {
        this.http
          .get<ChatSnippet[]>(`api/conversations/${conv.id}/messages`)
          .pipe(
            map((list: ChatSnippet[]) => {
              return list.length > 0 ? list[list.length - 1] : null;
            }),
          )
          .subscribe(msg => {
            let preview = 'New Conversation';
            if (msg) {
              const prefix = msg.profileDetails?.id === this.viewerProfileId ? 'You: ' : `${this.vibeTag(msg.profileDetails?.id ?? 0)}: `;
              preview = prefix + (msg.content ?? '');
            }
            this.buzzCache.set(conv.id, preview);
            this.conversations = [...this.conversations!];
          });
      }
    });
  }
}
