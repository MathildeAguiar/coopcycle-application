import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'commande',
        data: { pageTitle: 'coopCycleApp.commande.home.title' },
        loadChildren: () => import('./commande/commande.module').then(m => m.CommandeModule),
      },
      {
        path: 'client',
        data: { pageTitle: 'coopCycleApp.client.home.title' },
        loadChildren: () => import('./client/client.module').then(m => m.ClientModule),
      },
      {
        path: 'restaurateur',
        data: { pageTitle: 'coopCycleApp.restaurateur.home.title' },
        loadChildren: () => import('./restaurateur/restaurateur.module').then(m => m.RestaurateurModule),
      },
      {
        path: 'livreur',
        data: { pageTitle: 'coopCycleApp.livreur.home.title' },
        loadChildren: () => import('./livreur/livreur.module').then(m => m.LivreurModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
