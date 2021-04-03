import * as dayjs from 'dayjs';
import { IRestaurateur } from 'app/entities/restaurateur/restaurateur.model';
import { ILivreur } from 'app/entities/livreur/livreur.model';
import { IClient } from 'app/entities/client/client.model';

export interface ICommande {
  id?: number;
  nCommande?: number;
  date?: dayjs.Dayjs;
  contenu?: string;
  montant?: number;
  restaurateur?: IRestaurateur | null;
  livreur?: ILivreur | null;
  client?: IClient | null;
}

export class Commande implements ICommande {
  constructor(
    public id?: number,
    public nCommande?: number,
    public date?: dayjs.Dayjs,
    public contenu?: string,
    public montant?: number,
    public restaurateur?: IRestaurateur | null,
    public livreur?: ILivreur | null,
    public client?: IClient | null
  ) {}
}

export function getCommandeIdentifier(commande: ICommande): number | undefined {
  return commande.id;
}
