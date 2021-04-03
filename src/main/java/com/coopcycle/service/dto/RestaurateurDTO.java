package com.coopcycle.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.coopcycle.domain.Restaurateur} entity.
 */
public class RestaurateurDTO implements Serializable {

    private Long id;

    @NotNull
    private Long nRestaurant;

    @NotNull
    private String adresseRestaurant;

    @NotNull
    private String nomRestaurant;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getnRestaurant() {
        return nRestaurant;
    }

    public void setnRestaurant(Long nRestaurant) {
        this.nRestaurant = nRestaurant;
    }

    public String getAdresseRestaurant() {
        return adresseRestaurant;
    }

    public void setAdresseRestaurant(String adresseRestaurant) {
        this.adresseRestaurant = adresseRestaurant;
    }

    public String getNomRestaurant() {
        return nomRestaurant;
    }

    public void setNomRestaurant(String nomRestaurant) {
        this.nomRestaurant = nomRestaurant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RestaurateurDTO)) {
            return false;
        }

        RestaurateurDTO restaurateurDTO = (RestaurateurDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, restaurateurDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RestaurateurDTO{" +
            "id=" + getId() +
            ", nRestaurant=" + getnRestaurant() +
            ", adresseRestaurant='" + getAdresseRestaurant() + "'" +
            ", nomRestaurant='" + getNomRestaurant() + "'" +
            "}";
    }
}
