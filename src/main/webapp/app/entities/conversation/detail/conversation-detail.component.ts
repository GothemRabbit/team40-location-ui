import { Component, Input, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import dayjs from 'dayjs/esm';
import relativeTime from 'dayjs/esm/plugin/relativeTime';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IConversation } from '../conversation.model';
import { NewMessage } from 'app/entities/message/message.model';
import { MessageService } from 'app/entities/message/service/message.service';
import { AccountService } from 'app/core/auth/account.service';
import { IProfileDetails } from 'app/entities/profile-details/profile-details.model';

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
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ConversationDetailComponent implements OnInit {
  @Input() conversation: IConversation | null = null;
  messages: MessageDTO[] = [];
  loading = false;
  currentUserId = 1;
  newMessageContent = '';
  otherProfileDetails: IProfileDetails[] = [];

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
    this.setupUsers();
  }

  setCurrentUserId(): void {
    this.currentUserId = this.accountService.getCurrentUserId();
  }

  loadMessages(conversationId: number): void {
    this.loading = true;
    this.http.get<MessageDTO[]>(`api/conversations/${conversationId}/messages`).subscribe({
      next: data => (this.messages = data),
      error: err => console.error('Failed to load messages:', err),
      complete: () => (this.loading = false),
    });
  }

  previousState(): void {
    window.history.back();
  }

  sendMessage(): void {
    if (!this.conversation || !this.newMessageContent.trim()) {
      return;
    }

    const newMessage: NewMessage = {
      id: null,
      content: this.newMessageContent,
      timestamp: dayjs(),
      isRead: false,
      conversation: { id: this.conversation.id },
      profileDetails: { id: this.currentUserId },
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
    if (this.conversation?.profileDetails) {
      const [profileDetails1, profileDetails2] = this.conversation.profileDetails;

      this.http.get<IProfileDetails[]>(`api/profile-details?id.in=${profileDetails1.id},${profileDetails2.id}`).subscribe(profiles => {
        this.otherProfileDetails = profiles;
      });
    }
  }
}
