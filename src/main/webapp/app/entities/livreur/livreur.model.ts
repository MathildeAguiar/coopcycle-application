import { ICommande } from 'app/entities/commande/commande.model';

export interface ILivreur {
  id?: number;
  numLivreur?: number;
  nomLivreur?: string;
  positionGPS?: string;
  commandes?: ICommande[] | null;
}

export class Livreur implements ILivreur {
  constructor(
    public id?: number,
    public numLivreur?: number,
    public nomLivreur?: string,
    public positionGPS?: string,
    public commandes?: ICommande[] | null
  ) {}
}

export function getLivreurIdentifier(livreur: ILivreur): number | undefined {
  return livreur.id;
}
