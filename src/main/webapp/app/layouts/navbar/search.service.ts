import { Injectable, signal } from '@angular/core';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class SearchService {
  searchTerm = signal<string>('');

  constructor(private router: Router) {}

  setSearchTerm(value: string): void {
    this.searchTerm.set(value);
  }

  // Navigate to the home page with the search term in the query params
  navigateToSearchResults(): void {
    const term = this.searchTerm();
    if (term) {
      this.router.navigate(['/'], { queryParams: { search: term } });
    }
  }
}
