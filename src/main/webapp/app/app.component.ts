import { Component, inject, AfterViewInit, HostListener, OnInit } from '@angular/core';
import { registerLocaleData } from '@angular/common';
import { Router, NavigationEnd } from '@angular/router';
import dayjs from 'dayjs/esm';
import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { NgbDatepickerConfig } from '@ng-bootstrap/ng-bootstrap';
import locale from '@angular/common/locales/en';
// jhipster-needle-angular-add-module-import JHipster will add new module here

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { fontAwesomeIcons } from './config/font-awesome-icons';
import MainComponent from './layouts/main/main.component';

@Component({
  standalone: true,
  selector: 'jhi-app',
  template: '<jhi-main></jhi-main>',
  imports: [
    MainComponent,
    // jhipster-needle-angular-add-module JHipster will add new module here
  ],
})
export default class AppComponent implements AfterViewInit, OnInit {
  // --- Accessibility: Keyboard navigation state ---
  htmlElements: HTMLElement[] = [];
  currentIndex = 0;

  private readonly applicationConfigService = inject(ApplicationConfigService);
  private readonly iconLibrary = inject(FaIconLibrary);
  private readonly dpConfig = inject(NgbDatepickerConfig);
  private readonly router = inject(Router);

  constructor() {
    this.applicationConfigService.setEndpointPrefix(SERVER_API_URL);
    registerLocaleData(locale);
    this.iconLibrary.addIcons(...fontAwesomeIcons);
    this.dpConfig.minDate = { year: dayjs().subtract(100, 'year').year(), month: 1, day: 1 };
    this.router.events.subscribe(evt => {
      if (evt instanceof NavigationEnd) {
        this.refreshFocusable();
        this.currentIndex = 0;
        this.htmlElements[0]?.focus();
      }
    });
  }

  /**
   * After view initialization, collect all focusable elements.
   */
  ngAfterViewInit(): void {
    this.refreshFocusable();
    this.htmlElements[0]?.focus();
  }

  ngOnInit(): void {
    const dark = localStorage.getItem('darkMode') === 'true';
    if (dark) {
      document.body.classList.add('dark-mode');
    }
  }

  /**
   * Listen for arrow key events and move focus accordingly.
   */
  @HostListener('window:keydown', ['$event'])
  handleEvent(event: KeyboardEvent): void {
    const key = event.key;
    const activeEl = document.activeElement as HTMLElement | null;
    if (key === 'Enter' && activeEl) {
      const isButton = activeEl instanceof HTMLButtonElement;
      const isLink = activeEl instanceof HTMLAnchorElement && activeEl.hasAttribute('href');
      const isCheckbox = activeEl instanceof HTMLInputElement && activeEl.type === 'checkbox';
      const isRadio = activeEl instanceof HTMLInputElement && activeEl.type === 'radio';
      const isLabel = activeEl instanceof HTMLLabelElement && activeEl.classList.contains('drop-area');

      if (isButton || isLink || isCheckbox || isRadio || isLabel) {
        event.preventDefault();
        activeEl.click();
      }
      return;
    }

    if (!['ArrowUp', 'ArrowDown', 'ArrowLeft', 'ArrowRight'].includes(key)) {
      return;
    }
    event.preventDefault();

    this.refreshFocusable();

    const navContainer = document.querySelector('nav.navbar');
    const mainContainer = document.querySelector('main#content');
    if (navContainer && key === 'ArrowDown' && activeEl && navContainer.contains(activeEl)) {
      // first element inside your <main id="content">
      const firstInMain = this.htmlElements.find(el => mainContainer?.contains(el));
      if (firstInMain) {
        this.currentIndex = this.htmlElements.indexOf(firstInMain);
        firstInMain.focus();
      }
      return;
    }
    if (navContainer && key === 'ArrowUp' && activeEl && !navContainer.contains(activeEl)) {
      const navItems = this.htmlElements.filter(el => navContainer.contains(el));
      if (navItems.length) {
        const lastNavItem = navItems[navItems.length - 1];
        this.currentIndex = this.htmlElements.indexOf(lastNavItem);
        lastNavItem.focus();
      }
      return;
    }
    const idx = activeEl ? this.htmlElements.indexOf(activeEl) : -1;
    if (idx === -1) {
      // fallback to the very first element
      this.currentIndex = 0;
      this.htmlElements[0]?.focus();
      return;
    }
    const newIndex = key === 'ArrowUp' || key === 'ArrowLeft' ? Math.max(0, idx - 1) : Math.min(this.htmlElements.length - 1, idx + 1);

    // if (navContainer && (key === 'ArrowDown' || key === 'ArrowRight') && activeEl && navContainer.contains(activeEl)) {
    //   const target = this.htmlElements.find(el => !navContainer.contains(el));
    //   if (target) {
    //     this.currentIndex = this.htmlElements.indexOf(target);
    //     target.focus();
    //     return;
    //   }
    // }
    // // If navigating up/left from within main content, jump to last navbar element
    // if (navContainer && (key === 'ArrowUp' || key === 'ArrowLeft') && activeEl && !navContainer.contains(activeEl)) {
    //   const navItems = this.htmlElements.filter(el => navContainer.contains(el));
    //   const lastNav = navItems[navItems.length - 1];
    //   if (navItems.length > 0) {
    //     const lastNavItem = navItems[navItems.length - 1];
    //     this.currentIndex = this.htmlElements.indexOf(lastNavItem);
    //     lastNavItem.focus();
    //     return;
    //   }
    // }
    //
    // // Determine current index from actual focused element
    // let idx = activeEl ? this.htmlElements.indexOf(activeEl) : -1;
    // if (idx === -1) {
    //   idx = 0;
    //   this.htmlElements[0]?.focus();
    //   return;
    // }
    //
    // // Compute next index in linear list
    // const newIndex = key === 'ArrowUp' || key === 'ArrowLeft' ? Math.max(0, idx - 1) : Math.min(this.htmlElements.length - 1, idx + 1);

    if (newIndex !== idx) {
      this.currentIndex = newIndex;
      this.htmlElements[newIndex].focus();
    }
  }

  /**
   * Uses a comprehensive selector + visibility filter to return only truly focusable elements.
   */
  private getFocusableElements(root: HTMLElement): HTMLElement[] {
    const selector = [
      'a[href]',
      'area[href]',
      'input:not([disabled]):not([type="hidden"])',
      'select:not([disabled])',
      'textarea:not([disabled])',
      'button:not([disabled])',
      'iframe',
      'object',
      'embed',
      '[tabindex]:not([tabindex="-1"])',
      '[contenteditable]',
    ].join(',');
    return Array.from(root.querySelectorAll<HTMLElement>(selector)).filter(
      el => !!(el.offsetWidth || el.offsetHeight || el.getClientRects().length),
    );
  }
  /**
   * Helper to refresh the focusable elements list
   */
  private refreshFocusable(): void {
    this.htmlElements = this.getFocusableElements(document.body);
  }
}
