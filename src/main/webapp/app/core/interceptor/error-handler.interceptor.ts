import { Injectable, inject } from '@angular/core';
import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';

@Injectable()
export class ErrorHandlerInterceptor implements HttpInterceptor {
  private readonly eventManager = inject(EventManager);

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(
      tap({
        error: (err: HttpErrorResponse) => {
          const nahAuth = err.status === 401 && (err.message === '' || err.url?.includes('api/account'));
          const ghostChats = err.status === 500 && JSON.stringify(err.error ?? '').includes('REL_USER_DETAILS__CHATS');

          if (!nahAuth && !ghostChats) {
            this.eventManager.broadcast(new EventWithContent('teamproject24App.httpError', err));
          }
        },
      }),
    );
  }
}
