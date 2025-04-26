import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LocationComponent } from './list/location.component'; // 这是 standalone 组件

@NgModule({
  imports: [
    CommonModule,
    LocationComponent, // ✅ standalone 组件放 imports，不放 declarations
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class LocationModule {}
