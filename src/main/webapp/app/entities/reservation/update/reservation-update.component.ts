import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ReservationStatus } from 'app/entities/enumerations/reservation-status.model';
import { IReservation } from '../reservation.model';
import { ReservationService } from '../service/reservation.service';
import { ReservationFormGroup, ReservationFormService } from './reservation-form.service';

@Component({
  standalone: true,
  selector: 'jhi-reservation-update',
  templateUrl: './reservation-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ReservationUpdateComponent implements OnInit {
  isSaving = false;
  reservation: IReservation | null = null;
  reservationStatusValues = Object.keys(ReservationStatus);

  protected reservationService = inject(ReservationService);
  protected reservationFormService = inject(ReservationFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: ReservationFormGroup = this.reservationFormService.createReservationFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ reservation }) => {
      this.reservation = reservation;
      if (reservation) {
        this.updateForm(reservation);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const reservation = this.reservationFormService.getReservation(this.editForm);
    if (reservation.id !== null) {
      this.subscribeToSaveResponse(this.reservationService.update(reservation));
    } else {
      this.subscribeToSaveResponse(this.reservationService.create(reservation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReservation>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(reservation: IReservation): void {
    this.reservation = reservation;
    this.reservationFormService.resetForm(this.editForm, reservation);
  }
}
