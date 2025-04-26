import { Injectable, inject } from '@angular/core';
import { BehaviorSubject, Observable, switchMap, tap, from, of, map } from 'rxjs';

import { Account } from 'app/core/auth/account.model';
import { AccountService } from 'app/core/auth/account.service';
import { AuthServerProvider } from 'app/core/auth/auth-jwt.service';
import { Login } from './login.model';
import { ProfileDetailsService } from '../entities/profile-details/service/profile-details.service';
import { IProfileDetails } from '../entities/profile-details/profile-details.model';
import { catchError } from 'rxjs/operators';

const LS_KEY_PROFILE = 'cachedProfile';

@Injectable({ providedIn: 'root' })
export class LoginService {
  private readonly accountService = inject(AccountService);
  private readonly authServerProvider = inject(AuthServerProvider);
  private readonly profileDetailsService = inject(ProfileDetailsService);
  private profileDetails: BehaviorSubject<IProfileDetails | undefined> = new BehaviorSubject<IProfileDetails | undefined>(undefined);

  login(credentials: Login): Observable<Account | null> {
    return this.authServerProvider.login(credentials).pipe(
      switchMap(() => this.accountService.identity(true)),
      switchMap((account: Account | null) => {
        if (account?.authorities.includes('ROLE_ADMIN')) {
          this.profileDetails.next(undefined);
          return of(account);
        } else {
          return this.profileDetailsService.getCurrentUserProfile().pipe(
            tap((profile: IProfileDetails) => {
              this.profileDetails.next(profile);
              localStorage.setItem(LS_KEY_PROFILE, JSON.stringify(profile));
            }),
            map(() => account),
            catchError((err): Observable<Account | null> => {
              return of<Account | null>(null);
            }),
          );
        }
      }),
    );
  }

  logout(): void {
    from(this.authServerProvider.logout()).subscribe({
      complete: () => {
        this.accountService.authenticate(null);
        this.profileDetails.next(undefined);
        localStorage.removeItem(LS_KEY_PROFILE);
      },
    });
  }

  getProfileDetails(): Observable<IProfileDetails | undefined> {
    return this.profileDetails.asObservable();
  }
}
