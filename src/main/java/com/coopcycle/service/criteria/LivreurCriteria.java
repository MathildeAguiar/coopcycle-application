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
 * Criteria class for the {@link com.coopcycle.domain.Livreur} entity. This class is used
 * in {@link com.coopcycle.web.rest.LivreurResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /livreurs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LivreurCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter numLivreur;

    private StringFilter nomLivreur;

    private StringFilter positionGPS;

    private LongFilter commandeId;

    public LivreurCriteria() {}

    public LivreurCriteria(LivreurCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.numLivreur = other.numLivreur == null ? null : other.numLivreur.copy();
        this.nomLivreur = other.nomLivreur == null ? null : other.nomLivreur.copy();
        this.positionGPS = other.positionGPS == null ? null : other.positionGPS.copy();
        this.commandeId = other.commandeId == null ? null : other.commandeId.copy();
    }

    @Override
    public LivreurCriteria copy() {
        return new LivreurCriteria(this);
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

    public IntegerFilter getNumLivreur() {
        return numLivreur;
    }

    public IntegerFilter numLivreur() {
        if (numLivreur == null) {
            numLivreur = new IntegerFilter();
        }
        return numLivreur;
    }

    public void setNumLivreur(IntegerFilter numLivreur) {
        this.numLivreur = numLivreur;
    }

    public StringFilter getNomLivreur() {
        return nomLivreur;
    }

    public StringFilter nomLivreur() {
        if (nomLivreur == null) {
            nomLivreur = new StringFilter();
        }
        return nomLivreur;
    }

    public void setNomLivreur(StringFilter nomLivreur) {
        this.nomLivreur = nomLivreur;
    }

    public StringFilter getPositionGPS() {
        return positionGPS;
    }

    public StringFilter positionGPS() {
        if (positionGPS == null) {
            positionGPS = new StringFilter();
        }
        return positionGPS;
    }

    public void setPositionGPS(StringFilter positionGPS) {
        this.positionGPS = positionGPS;
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
        final LivreurCriteria that = (LivreurCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(numLivreur, that.numLivreur) &&
            Objects.equals(nomLivreur, that.nomLivreur) &&
            Objects.equals(positionGPS, that.positionGPS) &&
            Objects.equals(commandeId, that.commandeId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, numLivreur, nomLivreur, positionGPS, commandeId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LivreurCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (numLivreur != null ? "numLivreur=" + numLivreur + ", " : "") +
            (nomLivreur != null ? "nomLivreur=" + nomLivreur + ", " : "") +
            (positionGPS != null ? "positionGPS=" + positionGPS + ", " : "") +
            (commandeId != null ? "commandeId=" + commandeId + ", " : "") +
            "}";
    }
}
