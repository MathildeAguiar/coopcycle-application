package com.coopcycle.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.coopcycle.domain.Commande} entity. This class is used
 * in {@link com.coopcycle.web.rest.CommandeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /commandes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CommandeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter nCommande;

    private LocalDateFilter date;

    private StringFilter contenu;

    private LongFilter montant;

    private LongFilter restaurateurId;

    private LongFilter livreurId;

    private LongFilter clientId;

    public CommandeCriteria() {}

    public CommandeCriteria(CommandeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nCommande = other.nCommande == null ? null : other.nCommande.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.contenu = other.contenu == null ? null : other.contenu.copy();
        this.montant = other.montant == null ? null : other.montant.copy();
        this.restaurateurId = other.restaurateurId == null ? null : other.restaurateurId.copy();
        this.livreurId = other.livreurId == null ? null : other.livreurId.copy();
        this.clientId = other.clientId == null ? null : other.clientId.copy();
    }

    @Override
    public CommandeCriteria copy() {
        return new CommandeCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getnCommande() {
        return nCommande;
    }

    public LongFilter nCommande() {
        if (nCommande == null) {
            nCommande = new LongFilter();
        }
        return nCommande;
    }

    public void setnCommande(LongFilter nCommande) {
        this.nCommande = nCommande;
    }

    public LocalDateFilter getDate() {
        return date;
    }

    public LocalDateFilter date() {
        if (date == null) {
            date = new LocalDateFilter();
        }
        return date;
    }

    public void setDate(LocalDateFilter date) {
        this.date = date;
    }

    public StringFilter getContenu() {
        return contenu;
    }

    public StringFilter contenu() {
        if (contenu == null) {
            contenu = new StringFilter();
        }
        return contenu;
    }

    public void setContenu(StringFilter contenu) {
        this.contenu = contenu;
    }

    public LongFilter getMontant() {
        return montant;
    }

    public LongFilter montant() {
        if (montant == null) {
            montant = new LongFilter();
        }
        return montant;
    }

    public void setMontant(LongFilter montant) {
        this.montant = montant;
    }

    public LongFilter getRestaurateurId() {
        return restaurateurId;
    }

    public LongFilter restaurateurId() {
        if (restaurateurId == null) {
            restaurateurId = new LongFilter();
        }
        return restaurateurId;
    }

    public void setRestaurateurId(LongFilter restaurateurId) {
        this.restaurateurId = restaurateurId;
    }

    public LongFilter getLivreurId() {
        return livreurId;
    }

    public LongFilter livreurId() {
        if (livreurId == null) {
            livreurId = new LongFilter();
        }
        return livreurId;
    }

    public void setLivreurId(LongFilter livreurId) {
        this.livreurId = livreurId;
    }

    public LongFilter getClientId() {
        return clientId;
    }

    public LongFilter clientId() {
        if (clientId == null) {
            clientId = new LongFilter();
        }
        return clientId;
    }

    public void setClientId(LongFilter clientId) {
        this.clientId = clientId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CommandeCriteria that = (CommandeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nCommande, that.nCommande) &&
            Objects.equals(date, that.date) &&
            Objects.equals(contenu, that.contenu) &&
            Objects.equals(montant, that.montant) &&
            Objects.equals(restaurateurId, that.restaurateurId) &&
            Objects.equals(livreurId, that.livreurId) &&
            Objects.equals(clientId, that.clientId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nCommande, date, contenu, montant, restaurateurId, livreurId, clientId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CommandeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nCommande != null ? "nCommande=" + nCommande + ", " : "") +
            (date != null ? "date=" + date + ", " : "") +
            (contenu != null ? "contenu=" + contenu + ", " : "") +
            (montant != null ? "montant=" + montant + ", " : "") +
            (restaurateurId != null ? "restaurateurId=" + restaurateurId + ", " : "") +
            (livreurId != null ? "livreurId=" + livreurId + ", " : "") +
            (clientId != null ? "clientId=" + clientId + ", " : "") +
            "}";
    }
}
