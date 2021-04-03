import { ICommande } from 'app/entities/commande/commande.model';

export interface IRestaurateur {
  id?: number;
  nRestaurant?: number;
  adresseRestaurant?: string;
  nomRestaurant?: string;
  commandes?: ICommande[] | null;
}

export class Restaurateur implements IRestaurateur {
  constructor(
    public id?: number,
    public nRestaurant?: number,
    public adresseRestaurant?: string,
    public nomRestaurant?: string,
    public commandes?: ICommande[] | null
  ) {}
}

export function getRestaurateurIdentifier(restaurateur: IRestaurateur): number | undefined {
  return restaurateur.id;
}
