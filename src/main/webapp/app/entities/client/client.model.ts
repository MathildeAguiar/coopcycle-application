import { ICommande } from 'app/entities/commande/commande.model';

export interface IClient {
  id?: number;
  nom?: string;
  adresse?: string;
  telephone?: string | null;
  nClient?: number;
  commandes?: ICommande[] | null;
}

export class Client implements IClient {
  constructor(
    public id?: number,
    public nom?: string,
    public adresse?: string,
    public telephone?: string | null,
    public nClient?: number,
    public commandes?: ICommande[] | null
  ) {}
}

export function getClientIdentifier(client: IClient): number | undefined {
  return client.id;
}
