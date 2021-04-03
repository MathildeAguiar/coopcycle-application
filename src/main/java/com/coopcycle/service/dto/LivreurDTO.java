package com.coopcycle.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.coopcycle.domain.Livreur} entity.
 */
public class LivreurDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer numLivreur;

    @NotNull
    private String nomLivreur;

    @NotNull
    private String positionGPS;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumLivreur() {
        return numLivreur;
    }

    public void setNumLivreur(Integer numLivreur) {
        this.numLivreur = numLivreur;
    }

    public String getNomLivreur() {
        return nomLivreur;
    }

    public void setNomLivreur(String nomLivreur) {
        this.nomLivreur = nomLivreur;
    }

    public String getPositionGPS() {
        return positionGPS;
    }

    public void setPositionGPS(String positionGPS) {
        this.positionGPS = positionGPS;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LivreurDTO)) {
            return false;
        }

        LivreurDTO livreurDTO = (LivreurDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, livreurDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LivreurDTO{" +
            "id=" + getId() +
            ", numLivreur=" + getNumLivreur() +
            ", nomLivreur='" + getNomLivreur() + "'" +
            ", positionGPS='" + getPositionGPS() + "'" +
            "}";
    }
}
