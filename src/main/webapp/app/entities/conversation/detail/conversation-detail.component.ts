import { Component, Input, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { HttpClient } from '@angular/common/http';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IConversation } from '../conversation.model';

interface MessageDTO {
  id?: number;
  content?: string;
  timestamp?: string;
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

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    if (this.conversation) {
      this.loadMessages(this.conversation.id);
    }
  }

  loadMessages(conversationId: number): void {
    this.loading = true;
    this.http.get<MessageDTO[]>(`api/conversations/${conversationId}/messages`).subscribe({
      next: data => (this.messages = data),
      error: err => console.error(err),
      complete: () => (this.loading = false),
    });
  }

  previousState(): void {
    window.history.back();
  }
}
