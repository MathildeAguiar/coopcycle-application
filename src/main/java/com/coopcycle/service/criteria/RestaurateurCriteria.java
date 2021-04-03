package com.coopcycle.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.coopcycle.domain.Restaurateur} entity. This class is used
 * in {@link com.coopcycle.web.rest.RestaurateurResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /restaurateurs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RestaurateurCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter nRestaurant;

    private StringFilter adresseRestaurant;

    private StringFilter nomRestaurant;

    private LongFilter commandeId;

    public RestaurateurCriteria() {}

    public RestaurateurCriteria(RestaurateurCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.nRestaurant = other.nRestaurant == null ? null : other.nRestaurant.copy();
        this.adresseRestaurant = other.adresseRestaurant == null ? null : other.adresseRestaurant.copy();
        this.nomRestaurant = other.nomRestaurant == null ? null : other.nomRestaurant.copy();
        this.commandeId = other.commandeId == null ? null : other.commandeId.copy();
    }

    @Override
    public RestaurateurCriteria copy() {
        return new RestaurateurCriteria(this);
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

    public LongFilter getnRestaurant() {
        return nRestaurant;
    }

    public LongFilter nRestaurant() {
        if (nRestaurant == null) {
            nRestaurant = new LongFilter();
        }
        return nRestaurant;
    }

    public void setnRestaurant(LongFilter nRestaurant) {
        this.nRestaurant = nRestaurant;
    }

    public StringFilter getAdresseRestaurant() {
        return adresseRestaurant;
    }

    public StringFilter adresseRestaurant() {
        if (adresseRestaurant == null) {
            adresseRestaurant = new StringFilter();
        }
        return adresseRestaurant;
    }

    public void setAdresseRestaurant(StringFilter adresseRestaurant) {
        this.adresseRestaurant = adresseRestaurant;
    }

    public StringFilter getNomRestaurant() {
        return nomRestaurant;
    }

    public StringFilter nomRestaurant() {
        if (nomRestaurant == null) {
            nomRestaurant = new StringFilter();
        }
        return nomRestaurant;
    }

    public void setNomRestaurant(StringFilter nomRestaurant) {
        this.nomRestaurant = nomRestaurant;
    }

    public LongFilter getCommandeId() {
        return commandeId;
    }

    public LongFilter commandeId() {
        if (commandeId == null) {
            commandeId = new LongFilter();
        }
        return commandeId;
    }

    public void setCommandeId(LongFilter commandeId) {
        this.commandeId = commandeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final RestaurateurCriteria that = (RestaurateurCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nRestaurant, that.nRestaurant) &&
            Objects.equals(adresseRestaurant, that.adresseRestaurant) &&
            Objects.equals(nomRestaurant, that.nomRestaurant) &&
            Objects.equals(commandeId, that.commandeId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nRestaurant, adresseRestaurant, nomRestaurant, commandeId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RestaurateurCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (nRestaurant != null ? "nRestaurant=" + nRestaurant + ", " : "") +
            (adresseRestaurant != null ? "adresseRestaurant=" + adresseRestaurant + ", " : "") +
            (nomRestaurant != null ? "nomRestaurant=" + nomRestaurant + ", " : "") +
            (commandeId != null ? "commandeId=" + commandeId + ", " : "") +
            "}";
    }
}
