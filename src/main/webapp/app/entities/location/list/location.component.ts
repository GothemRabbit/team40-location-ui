import { Component, NgZone, OnInit, AfterViewInit, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { Subscription, filter, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { inject } from '@angular/core';

import SharedModule from 'app/shared/shared.module';
import { FormsModule } from '@angular/forms';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ILocation } from '../location.model';
import { LocationService } from '../service/location.service';
import { LocationDeleteDialogComponent } from '../delete/location-delete-dialog.component';

declare const google: any;

@Component({
  standalone: true,
  selector: 'jhi-location',
  templateUrl: './location.component.html',
  imports: [RouterModule, FormsModule, SharedModule],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class LocationComponent implements OnInit, AfterViewInit {
  public readonly router = inject(Router);

  showOverviewMap = false;
  visibleCount = 0;
  hiddenMaps: Record<number, boolean> = {};
  markers: { position: { lat: number; lng: number }; title: string }[] = [];
  overviewOptions = {
    center: { lat: 31.2304, lng: 121.4737 },
    zoom: 10,
  };
  isLoading = false;

  protected readonly locationService = inject(LocationService);
  protected readonly activatedRoute = inject(ActivatedRoute);
  protected modalService = inject(NgbModal);
  protected ngZone = inject(NgZone);

  private _filterText = '';
  private subscription: Subscription | null = null;
  private locations: ILocation[] = [];

  get filterText(): string {
    return this._filterText;
  }

  set filterText(value: string) {
    this._filterText = value;
    this.visibleCount = Math.min(this.filteredLocations.length, Math.max(4, this.visibleCount));
  }

  get filteredLocations(): ILocation[] {
    const search = this._filterText.toLowerCase().trim();
    if (!search) return this.locations;
    return this.locations.filter(location => {
      return (
        (location.address?.toLowerCase().includes(search) ?? false) ||
        (location.postcode?.toLowerCase().includes(search) ?? false) ||
        (location.users?.some((user: { id: number }) => user.id.toString().toLowerCase().includes(search)) ?? false) ||
        `location #${location.id}`.toLowerCase().includes(search)
      );
    });
  }

  get visibleLocations(): ILocation[] {
    return this.filteredLocations.slice(0, this.visibleCount);
  }

  ngOnInit(): void {
    this.load();
  }

  ngAfterViewInit(): void {
    const waitForGoogle = (): void => {
      if ((window as any).googleMapsReady && typeof google !== 'undefined' && google.maps) {
        this.initMaps();
      } else {
        setTimeout(waitForGoogle, 100);
      }
    };
    waitForGoogle();
  }

  load(): void {
    this.isLoading = true;
    this.locationService
      .query()
      .pipe(tap(() => (this.isLoading = false)))
      .subscribe(res => {
        this.locations = res.body ?? [];
        this.visibleCount = Math.min(4, this.locations.length); // 修正为不依赖 filteredLocations
        this.markers = this.locations
          .filter(loc => this.isValidCoordinate(loc.latitude) && this.isValidCoordinate(loc.longitude))
          .map(loc => ({
            position: { lat: Number(loc.latitude!), lng: Number(loc.longitude!) },
            title: loc.address ?? '',
          }));
      });
  }

  loadMore(): void {
    this.visibleCount = Math.min(this.visibleCount + 1, this.filteredLocations.length);
  }

  toggleOverviewMap(): void {
    this.showOverviewMap = !this.showOverviewMap;
    if (this.showOverviewMap) {
      setTimeout(() => this.initOverviewMap(), 100);
    }
  }

  toggleCardMap(id: number): void {
    this.hiddenMaps[id] = !this.hiddenMaps[id];
  }

  delete(location: ILocation): void {
    const modalRef = this.modalService.open(LocationDeleteDialogComponent, {
      size: 'lg',
      backdrop: 'static',
    });
    modalRef.componentInstance.location = location;
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        tap(() => this.load()),
      )
      .subscribe();
  }

  private initMaps(): void {
    this.visibleLocations.forEach(location => {
      const mapElement = document.getElementById(`map-${location.id}`);
      if (mapElement) {
        const map = new google.maps.Map(mapElement, {
          center: { lat: Number(location.latitude), lng: Number(location.longitude) },
          zoom: 12,
        });

        void new google.maps.Marker({
          position: { lat: Number(location.latitude), lng: Number(location.longitude) },
          map,
          title: location.address ?? '',
        });
      }
    });
  }

  private initOverviewMap(): void {
    const mapElement = document.getElementById('overview-map');
    if (mapElement) {
      const map = new google.maps.Map(mapElement, {
        center: this.overviewOptions.center,
        zoom: this.overviewOptions.zoom,
      });

      this.markers = this.locations.map(loc => ({
        position: {
          lat: Number(loc.latitude ?? 31.2304),
          lng: Number(loc.longitude ?? 121.4737),
        },
        title: loc.address ?? '',
      }));
      this.markers.forEach(markerData => {
        void new google.maps.Marker({
          position: markerData.position,
          title: markerData.title,
          map,
        });
      });
    }
  }

  private isValidCoordinate(value: any): value is number {
    return !isNaN(Number(value));
  }
}
