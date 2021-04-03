import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IRestaurateur, Restaurateur } from '../restaurateur.model';
import { RestaurateurService } from '../service/restaurateur.service';

@Component({
  selector: 'jhi-restaurateur-update',
  templateUrl: './restaurateur-update.component.html',
})
export class RestaurateurUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nRestaurant: [null, [Validators.required]],
    adresseRestaurant: [null, [Validators.required]],
    nomRestaurant: [null, [Validators.required]],
  });

  constructor(protected restaurateurService: RestaurateurService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ restaurateur }) => {
      this.updateForm(restaurateur);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const restaurateur = this.createFromForm();
    if (restaurateur.id !== undefined) {
      this.subscribeToSaveResponse(this.restaurateurService.update(restaurateur));
    } else {
      this.subscribeToSaveResponse(this.restaurateurService.create(restaurateur));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRestaurateur>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
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

  protected updateForm(restaurateur: IRestaurateur): void {
    this.editForm.patchValue({
      id: restaurateur.id,
      nRestaurant: restaurateur.nRestaurant,
      adresseRestaurant: restaurateur.adresseRestaurant,
      nomRestaurant: restaurateur.nomRestaurant,
    });
  }

  protected createFromForm(): IRestaurateur {
    return {
      ...new Restaurateur(),
      id: this.editForm.get(['id'])!.value,
      nRestaurant: this.editForm.get(['nRestaurant'])!.value,
      adresseRestaurant: this.editForm.get(['adresseRestaurant'])!.value,
      nomRestaurant: this.editForm.get(['nomRestaurant'])!.value,
    };
  }
}
