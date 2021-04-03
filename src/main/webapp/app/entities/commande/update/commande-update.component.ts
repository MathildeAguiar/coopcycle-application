import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICommande, Commande } from '../commande.model';
import { CommandeService } from '../service/commande.service';
import { IRestaurateur } from 'app/entities/restaurateur/restaurateur.model';
import { RestaurateurService } from 'app/entities/restaurateur/service/restaurateur.service';
import { ILivreur } from 'app/entities/livreur/livreur.model';
import { LivreurService } from 'app/entities/livreur/service/livreur.service';
import { IClient } from 'app/entities/client/client.model';
import { ClientService } from 'app/entities/client/service/client.service';

@Component({
  selector: 'jhi-commande-update',
  templateUrl: './commande-update.component.html',
})
export class CommandeUpdateComponent implements OnInit {
  isSaving = false;

  restaurateursSharedCollection: IRestaurateur[] = [];
  livreursSharedCollection: ILivreur[] = [];
  clientsSharedCollection: IClient[] = [];

  editForm = this.fb.group({
    id: [],
    nCommande: [null, [Validators.required]],
    date: [null, [Validators.required]],
    contenu: [null, [Validators.required]],
    montant: [null, [Validators.required]],
    restaurateur: [],
    livreur: [],
    client: [],
  });

  constructor(
    protected commandeService: CommandeService,
    protected restaurateurService: RestaurateurService,
    protected livreurService: LivreurService,
    protected clientService: ClientService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ commande }) => {
      this.updateForm(commande);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const commande = this.createFromForm();
    if (commande.id !== undefined) {
      this.subscribeToSaveResponse(this.commandeService.update(commande));
    } else {
      this.subscribeToSaveResponse(this.commandeService.create(commande));
    }
  }

  trackRestaurateurById(index: number, item: IRestaurateur): number {
    return item.id!;
  }

  trackLivreurById(index: number, item: ILivreur): number {
    return item.id!;
  }

  trackClientById(index: number, item: IClient): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICommande>>): void {
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

  protected updateForm(commande: ICommande): void {
    this.editForm.patchValue({
      id: commande.id,
      nCommande: commande.nCommande,
      date: commande.date,
      contenu: commande.contenu,
      montant: commande.montant,
      restaurateur: commande.restaurateur,
      livreur: commande.livreur,
      client: commande.client,
    });

    this.restaurateursSharedCollection = this.restaurateurService.addRestaurateurToCollectionIfMissing(
      this.restaurateursSharedCollection,
      commande.restaurateur
    );
    this.livreursSharedCollection = this.livreurService.addLivreurToCollectionIfMissing(this.livreursSharedCollection, commande.livreur);
    this.clientsSharedCollection = this.clientService.addClientToCollectionIfMissing(this.clientsSharedCollection, commande.client);
  }

  protected loadRelationshipsOptions(): void {
    this.restaurateurService
      .query()
      .pipe(map((res: HttpResponse<IRestaurateur[]>) => res.body ?? []))
      .pipe(
        map((restaurateurs: IRestaurateur[]) =>
          this.restaurateurService.addRestaurateurToCollectionIfMissing(restaurateurs, this.editForm.get('restaurateur')!.value)
        )
      )
      .subscribe((restaurateurs: IRestaurateur[]) => (this.restaurateursSharedCollection = restaurateurs));

    this.livreurService
      .query()
      .pipe(map((res: HttpResponse<ILivreur[]>) => res.body ?? []))
      .pipe(
        map((livreurs: ILivreur[]) => this.livreurService.addLivreurToCollectionIfMissing(livreurs, this.editForm.get('livreur')!.value))
      )
      .subscribe((livreurs: ILivreur[]) => (this.livreursSharedCollection = livreurs));

    this.clientService
      .query()
      .pipe(map((res: HttpResponse<IClient[]>) => res.body ?? []))
      .pipe(map((clients: IClient[]) => this.clientService.addClientToCollectionIfMissing(clients, this.editForm.get('client')!.value)))
      .subscribe((clients: IClient[]) => (this.clientsSharedCollection = clients));
  }

  protected createFromForm(): ICommande {
    return {
      ...new Commande(),
      id: this.editForm.get(['id'])!.value,
      nCommande: this.editForm.get(['nCommande'])!.value,
      date: this.editForm.get(['date'])!.value,
      contenu: this.editForm.get(['contenu'])!.value,
      montant: this.editForm.get(['montant'])!.value,
      restaurateur: this.editForm.get(['restaurateur'])!.value,
      livreur: this.editForm.get(['livreur'])!.value,
      client: this.editForm.get(['client'])!.value,
    };
  }
}
