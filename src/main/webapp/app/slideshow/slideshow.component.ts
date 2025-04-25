import { Component, OnInit } from '@angular/core';

@Component({
  standalone: true,
  selector: 'jhi-slideshow',
  templateUrl: './slideshow.component.html',
  styleUrls: ['./slideshow.component.scss'],
})
export class SlideshowComponent implements OnInit {
  images: string[] = ['/content/images/sell1.png', '/content/images/buy2.png', '/content/images/buy1.png', '/content/images/sell2.png'];
  currentIndex = 0;

  ngOnInit(): void {
    setInterval(() => {
      this.nextImage();
    }, 6000); // Change image every 3 seconds
  }

  nextImage(): void {
    this.currentIndex = (this.currentIndex + 1) % this.images.length;
  }

  previousImage(): void {
    this.currentIndex = (this.currentIndex - 1 + this.images.length) % this.images.length;
  }
}
