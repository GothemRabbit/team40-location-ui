import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';

interface MessageDTO {
  id?: number;
  content?: string;
  timestamp?: string;
  username?: string;
}

@Component({
  selector: 'jhi-conversation-messages',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div>
      <h2>Conversation {{ conversationId }} - Messages</h2>
      <div *ngIf="loading">Loading messages...</div>
      <div *ngIf="!loading && messages.length === 0">No messages found.</div>
      <ul *ngIf="!loading && messages.length">
        <li *ngFor="let msg of messages">
          <strong>{{ msg.content }}</strong>
          <br />
          <small>{{ msg.timestamp }}</small>
        </li>
      </ul>
    </div>
  `,
})
export class ConversationMessagesComponent implements OnInit {
  conversationId?: number;
  messages: MessageDTO[] = [];
  loading = false;

  constructor(
    private route: ActivatedRoute,
    private http: HttpClient,
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const idParam = params.get('id');
      if (idParam) {
        this.conversationId = +idParam; // convert string to number
        this.loadMessages(this.conversationId);
      }
    });
  }

  loadMessages(id: number): void {
    this.loading = true;
    this.http.get<MessageDTO[]>(`api/conversations/${id}/messages`).subscribe({
      next: data => (this.messages = data),
      error: err => console.error(err),
      complete: () => (this.loading = false),
    });
  }
}
