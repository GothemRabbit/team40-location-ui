import { Component, input, AfterViewInit, inject } from '@angular/core';
import { RouterModule } from '@angular/router';
import { HttpClient } from '@angular/common/http';

import SharedModule from 'app/shared/shared.module';
import { ILocation } from '../location.model';

// ✅ 声明全局 google，防止 TS 报错
declare const google: any;

@Component({
  standalone: true,
  selector: 'jhi-location-detail',
  templateUrl: './location-detail.component.html',
  imports: [SharedModule, RouterModule], // ⛳ 如果用不到管道，就不引入多余的 pipe
})
export class LocationDetailComponent implements AfterViewInit {
  location = input<ILocation | null>(null);
  private http = inject(HttpClient);

  previousState(): void {
    window.history.back();
  }

  ngAfterViewInit(): void {
    // ⏳ 稍等 DOM 和数据渲染后再尝试加载地图
    setTimeout(() => {
      if (this.location()?.latitude && this.location()?.longitude) {
        this.loadMap();
      }
    }, 200);
  }

  loadMap(): void {
    // ⚙️ 请求后端接口获取 API 密钥
    this.http.get('/api/map-key', { responseType: 'text' }).subscribe(apiKey => {
      const script = document.createElement('script');
      script.src = `https://maps.googleapis.com/maps/api/js?key=${apiKey}&callback=initMap`;
      script.async = true;
      document.head.appendChild(script);

      // ⛳ 在全局注册地图初始化函数
      (window as any).initMap = () => {
        const loc = this.location();
        const lat = Number(loc?.latitude ?? 51.5074);
        const lng = Number(loc?.longitude ?? -0.1278);

        const map = new google.maps.Map(document.getElementById('map') as HTMLElement, {
          center: { lat, lng },
          zoom: 14,
        });

        const marker = new google.maps.Marker({
          position: { lat, lng },
          map,
          title: 'Location',
        });
      };
    });
  }
}
