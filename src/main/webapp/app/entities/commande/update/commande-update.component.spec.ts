jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CommandeService } from '../service/commande.service';
import { ICommande, Commande } from '../commande.model';
import { IRestaurateur } from 'app/entities/restaurateur/restaurateur.model';
import { RestaurateurService } from 'app/entities/restaurateur/service/restaurateur.service';
import { ILivreur } from 'app/entities/livreur/livreur.model';
import { LivreurService } from 'app/entities/livreur/service/livreur.service';
import { IClient } from 'app/entities/client/client.model';
import { ClientService } from 'app/entities/client/service/client.service';

import { CommandeUpdateComponent } from './commande-update.component';

describe('Component Tests', () => {
  describe('Commande Management Update Component', () => {
    let comp: CommandeUpdateComponent;
    let fixture: ComponentFixture<CommandeUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let commandeService: CommandeService;
    let restaurateurService: RestaurateurService;
    let livreurService: LivreurService;
    let clientService: ClientService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CommandeUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CommandeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CommandeUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      commandeService = TestBed.inject(CommandeService);
      restaurateurService = TestBed.inject(RestaurateurService);
      livreurService = TestBed.inject(LivreurService);
      clientService = TestBed.inject(ClientService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Restaurateur query and add missing value', () => {
        const commande: ICommande = { id: 456 };
        const restaurateur: IRestaurateur = { id: 96550 };
        commande.restaurateur = restaurateur;

        const restaurateurCollection: IRestaurateur[] = [{ id: 3456 }];
        spyOn(restaurateurService, 'query').and.returnValue(of(new HttpResponse({ body: restaurateurCollection })));
        const additionalRestaurateurs = [restaurateur];
        const expectedCollection: IRestaurateur[] = [...additionalRestaurateurs, ...restaurateurCollection];
        spyOn(restaurateurService, 'addRestaurateurToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ commande });
        comp.ngOnInit();

        expect(restaurateurService.query).toHaveBeenCalled();
        expect(restaurateurService.addRestaurateurToCollectionIfMissing).toHaveBeenCalledWith(
          restaurateurCollection,
          ...additionalRestaurateurs
        );
        expect(comp.restaurateursSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Livreur query and add missing value', () => {
        const commande: ICommande = { id: 456 };
        const livreur: ILivreur = { id: 38735 };
        commande.livreur = livreur;

        const livreurCollection: ILivreur[] = [{ id: 36550 }];
        spyOn(livreurService, 'query').and.returnValue(of(new HttpResponse({ body: livreurCollection })));
        const additionalLivreurs = [livreur];
        const expectedCollection: ILivreur[] = [...additionalLivreurs, ...livreurCollection];
        spyOn(livreurService, 'addLivreurToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ commande });
        comp.ngOnInit();

        expect(livreurService.query).toHaveBeenCalled();
        expect(livreurService.addLivreurToCollectionIfMissing).toHaveBeenCalledWith(livreurCollection, ...additionalLivreurs);
        expect(comp.livreursSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Client query and add missing value', () => {
        const commande: ICommande = { id: 456 };
        const client: IClient = { id: 35220 };
        commande.client = client;

        const clientCollection: IClient[] = [{ id: 91628 }];
        spyOn(clientService, 'query').and.returnValue(of(new HttpResponse({ body: clientCollection })));
        const additionalClients = [client];
        const expectedCollection: IClient[] = [...additionalClients, ...clientCollection];
        spyOn(clientService, 'addClientToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ commande });
        comp.ngOnInit();

        expect(clientService.query).toHaveBeenCalled();
        expect(clientService.addClientToCollectionIfMissing).toHaveBeenCalledWith(clientCollection, ...additionalClients);
        expect(comp.clientsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const commande: ICommande = { id: 456 };
        const restaurateur: IRestaurateur = { id: 43222 };
        commande.restaurateur = restaurateur;
        const livreur: ILivreur = { id: 32061 };
        commande.livreur = livreur;
        const client: IClient = { id: 34946 };
        commande.client = client;

        activatedRoute.data = of({ commande });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(commande));
        expect(comp.restaurateursSharedCollection).toContain(restaurateur);
        expect(comp.livreursSharedCollection).toContain(livreur);
        expect(comp.clientsSharedCollection).toContain(client);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const commande = { id: 123 };
        spyOn(commandeService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ commande });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: commande }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(commandeService.update).toHaveBeenCalledWith(commande);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const commande = new Commande();
        spyOn(commandeService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ commande });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: commande }));
        saveSubject.complete();

        // THEN
        expect(commandeService.create).toHaveBeenCalledWith(commande);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const commande = { id: 123 };
        spyOn(commandeService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ commande });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(commandeService.update).toHaveBeenCalledWith(commande);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackRestaurateurById', () => {
        it('Should return tracked Restaurateur primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackRestaurateurById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackLivreurById', () => {
        it('Should return tracked Livreur primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackLivreurById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackClientById', () => {
        it('Should return tracked Client primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackClientById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
