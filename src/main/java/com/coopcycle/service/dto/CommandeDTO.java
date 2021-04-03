package com.coopcycle.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.coopcycle.domain.Commande} entity.
 */
public class CommandeDTO implements Serializable {

    private Long id;

    @NotNull
    private Long nCommande;

    @NotNull
    private LocalDate date;

    @NotNull
    private String contenu;

    @NotNull
    private Long montant;

    private RestaurateurDTO restaurateur;

    private LivreurDTO livreur;

    private ClientDTO client;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getnCommande() {
        return nCommande;
    }

    public void setnCommande(Long nCommande) {
        this.nCommande = nCommande;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public Long getMontant() {
        return montant;
    }

    public void setMontant(Long montant) {
        this.montant = montant;
    }

    public RestaurateurDTO getRestaurateur() {
        return restaurateur;
    }

    public void setRestaurateur(RestaurateurDTO restaurateur) {
        this.restaurateur = restaurateur;
    }

    public LivreurDTO getLivreur() {
        return livreur;
    }

    public void setLivreur(LivreurDTO livreur) {
        this.livreur = livreur;
    }

    public ClientDTO getClient() {
        return client;
    }

    public void setClient(ClientDTO client) {
        this.client = client;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CommandeDTO)) {
            return false;
        }

        CommandeDTO commandeDTO = (CommandeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, commandeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommandeDTO{" +
            "id=" + getId() +
            ", nCommande=" + getnCommande() +
            ", date='" + getDate() + "'" +
            ", contenu='" + getContenu() + "'" +
            ", montant=" + getMontant() +
            ", restaurateur=" + getRestaurateur() +
            ", livreur=" + getLivreur() +
            ", client=" + getClient() +
            "}";
    }
}
