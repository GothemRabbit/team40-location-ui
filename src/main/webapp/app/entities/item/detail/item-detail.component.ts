import { Component, inject, input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { DataUtils } from 'app/core/util/data-util.service';
import { IItem } from '../item.model';
import { ImagesComponent } from '../../images/list/images.component';
import { ItemService } from '../service/item.service';

@Component({
  standalone: true,
  selector: 'jhi-item-detail',
  templateUrl: './item-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe, ImagesComponent],
})
export class ItemDetailComponent {
  item = input<IItem | null>(null);

  protected dataUtils = inject(DataUtils);

  constructor(
    private route: ActivatedRoute,
    private itemService: ItemService,
  ) {}

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
