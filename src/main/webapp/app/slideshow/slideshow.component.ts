import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  standalone: true,
  selector: 'jhi-slideshow',
  templateUrl: './slideshow.component.html',
  styleUrls: ['./slideshow.component.scss'],
  imports: [CommonModule],
})
export class SlideshowComponent implements OnInit {
  images: string[] = ['/content/images/sell1.png', '/content/images/buy2.png', '/content/images/buy1.png', '/content/images/sell2.png'];
  currentIndex = 0;

  constructor(private router: Router) {}

  ngOnInit(): void {
    setInterval(() => {
      this.nextImage();
    }, 6000);
  }

  nextImage(): void {
    this.currentIndex = (this.currentIndex + 1) % this.images.length;
  }

  previousImage(): void {
    this.currentIndex = (this.currentIndex - 1 + this.images.length) % this.images.length;
  }

  onSpecialAction(): void {
    this.router.navigate(['/item/new']);
  }

  onAnotherAction(): void {
    this.router.navigate(['/login']);
  }
}
