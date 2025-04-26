import { Component, Input, OnInit, ElementRef, ViewChild, AfterViewInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import dayjs from 'dayjs/esm';
import relativeTime from 'dayjs/esm/plugin/relativeTime';
import { forkJoin } from 'rxjs';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IConversation } from '../conversation.model';
import { NewMessage } from 'app/entities/message/message.model';
import { MessageService } from 'app/entities/message/service/message.service';
import { AccountService } from 'app/core/auth/account.service';
import { IProfileDetails } from 'app/entities/profile-details/profile-details.model';
import HasAnyAuthorityDirective from 'app/shared/auth/has-any-authority.directive';

// Extend dayjs with the relativeTime plugin
dayjs.extend(relativeTime);

interface MessageDTO {
  id?: number;
  content?: string;
  timestamp?: string;
  username?: string;
  profileDetails?: { id: number };
}

@Component({
  standalone: true,
  selector: 'jhi-conversation-detail',
  templateUrl: './conversation-detail.component.html',
  styleUrls: ['./conversation-detail.component.css'],
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe, HasAnyAuthorityDirective],
})
export class ConversationDetailComponent implements OnInit, AfterViewInit {
  @Input() conversation: IConversation | null = null;
  messages: MessageDTO[] = [];
  loading = false;
  /** ID of the logged-in Spring Security user */
  currentUserId = 1;
  newMessageContent = '';
  hypeFellas: IProfileDetails[] = [];
  mainStageHomies: IProfileDetails[] = [];
  currentProfileId: number | null = null;
  currentUsername = '';

  @ViewChild('messagesContainer') private messagesRef!: ElementRef<HTMLDivElement>;

  constructor(
    private http: HttpClient,
    private messageService: MessageService,
    private accountService: AccountService,
  ) {}

  ngOnInit(): void {
    if (this.conversation) {
      this.loadMessages(this.conversation.id);
    }
    this.setCurrentUserId();
    this.currentUsername = this.accountService.getCurrentUserusername();
    this.setupUsers();
  }

  ngAfterViewInit(): void {
    setTimeout(() => this.scrollToBottom(), 0);
  }

  setCurrentUserId(): void {
    this.currentUserId = this.accountService.getCurrentUserId();
  }

  loadMessages(conversationId: number): void {
    this.loading = true;
    this.http.get<MessageDTO[]>(`api/conversations/${conversationId}/messages`).subscribe({
      next: data => {
        this.messages = this.finesseMessages(data);
        setTimeout(() => this.scrollToBottom());
      },
      error: err => console.error('Failed to load messages:', err),
      complete: () => (this.loading = false),
    });
  }

  getChatTitle(): string {
    if (this.mainStageHomies.length) {
      return this.mainStageHomies.map(p => p.userName ?? `User #${p.id}`).join(' & ');
    }
    return 'Conversation';
  }

  previousState(): void {
    window.history.back();
  }

  sendMessage(): void {
    if (!this.conversation || !this.newMessageContent.trim()) {
      return;
    }

    const profileIdForSend = this.currentProfileId ?? this.conversation.profileDetails?.[0]?.id ?? this.currentUserId;

    const newMessage: NewMessage = {
      id: null,
      content: this.newMessageContent,
      timestamp: dayjs(),
      isRead: false,
      conversation: { id: this.conversation.id },
      profileDetails: { id: profileIdForSend },
    };

    this.messageService.create(newMessage).subscribe({
      next: () => {
        this.newMessageContent = '';
        if (this.conversation) {
          this.loadMessages(this.conversation.id);
        }
      },
      error: err => console.error('Failed to send message:', err),
    });
  }

  setupUsers(): void {
    if (this.conversation?.profileDetails?.length) {
      const otherIds = this.conversation.participants?.map(p => p.id) ?? [];
      const idsArr = [...this.conversation.profileDetails.map(pd => pd.id), ...otherIds].slice(0, 2);

      const fetches = idsArr.map(id => this.http.get<IProfileDetails>(`api/profile-details/${id}`));

      forkJoin(fetches).subscribe(profiles => {
        this.handleProfilesLoaded(profiles);
      });
    }
  }

  isCurrentUser(msg: MessageDTO): boolean {
    if (this.currentProfileId !== null && msg.profileDetails?.id !== undefined) {
      return msg.profileDetails.id === this.currentProfileId;
    }
    return msg.username === this.currentUsername;
  }

  private handleProfilesLoaded(profiles: IProfileDetails[]): void {
    this.hypeFellas = profiles;

    const loggedInUserId = this.accountService.getCurrentUserId();
    const loggedInUsername = this.accountService.getCurrentUserusername();

    let myProfile = profiles.find(p => p.user?.id === loggedInUserId);
    if (!myProfile && loggedInUsername) {
      myProfile = profiles.find(p => p.userName === loggedInUsername);
    }

    this.currentProfileId = myProfile ? myProfile.id : null;

    if (myProfile) {
      const others = profiles.filter(p => p.id !== myProfile.id);
      this.mainStageHomies = [myProfile, ...(others.length ? [others[0]] : [])];
    } else {
      this.mainStageHomies = profiles.slice(0, 2);
    }

    this.messages = this.finesseMessages(this.messages);
  }

  private finesseMessages(rawMsgs: MessageDTO[]): MessageDTO[] {
    if (!this.hypeFellas.length) {
      return rawMsgs;
    }

    return rawMsgs.map(m => {
      if (!m.username) {
        const chatter = this.hypeFellas.find(h => h.id === m.profileDetails?.id);
        m.username = chatter?.userName ?? 'Anon';
      }
      return m;
    });
  }

  private scrollToBottom(): void {
    try {
      this.messagesRef.nativeElement.scrollTop = this.messagesRef.nativeElement.scrollHeight;
    } catch {
      // slaijfhasd;i
    }
  }
}
