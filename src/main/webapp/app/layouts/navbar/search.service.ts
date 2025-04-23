import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class SearchService {
  currentSearchTerm: Observable<string>; // Declare the public member first
  private searchTermSource = new BehaviorSubject<string>(''); // Then declare private member

  constructor() {
    this.currentSearchTerm = this.searchTermSource.asObservable(); // Assign the observable after declaration
  }

  setSearchTerm(term: string): void {
    this.searchTermSource.next(term);
  }

  getCurrentSearchTerm(): string {
    return this.searchTermSource.getValue();
  }
}
