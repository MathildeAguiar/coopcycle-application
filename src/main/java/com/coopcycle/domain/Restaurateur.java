package com.coopcycle.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Restaurateur.
 */
@Entity
@Table(name = "restaurateur")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Restaurateur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "n_restaurant", nullable = false, unique = true)
    private Long nRestaurant;

    @NotNull
    @Column(name = "adresse_restaurant", nullable = false)
    private String adresseRestaurant;

    @NotNull
    @Column(name = "nom_restaurant", nullable = false)
    private String nomRestaurant;

    @OneToMany(mappedBy = "restaurateur")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "restaurateur", "livreur", "client" }, allowSetters = true)
    private Set<Commande> commandes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Restaurateur id(Long id) {
        this.id = id;
        return this;
    }

    public Long getnRestaurant() {
        return this.nRestaurant;
    }

    public Restaurateur nRestaurant(Long nRestaurant) {
        this.nRestaurant = nRestaurant;
        return this;
    }

    public void setnRestaurant(Long nRestaurant) {
        this.nRestaurant = nRestaurant;
    }

    public String getAdresseRestaurant() {
        return this.adresseRestaurant;
    }

    public Restaurateur adresseRestaurant(String adresseRestaurant) {
        this.adresseRestaurant = adresseRestaurant;
        return this;
    }

    public void setAdresseRestaurant(String adresseRestaurant) {
        this.adresseRestaurant = adresseRestaurant;
    }

    public String getNomRestaurant() {
        return this.nomRestaurant;
    }

    public Restaurateur nomRestaurant(String nomRestaurant) {
        this.nomRestaurant = nomRestaurant;
        return this;
    }

    public void setNomRestaurant(String nomRestaurant) {
        this.nomRestaurant = nomRestaurant;
    }

    public Set<Commande> getCommandes() {
        return this.commandes;
    }

    public Restaurateur commandes(Set<Commande> commandes) {
        this.setCommandes(commandes);
        return this;
    }

    public Restaurateur addCommande(Commande commande) {
        this.commandes.add(commande);
        commande.setRestaurateur(this);
        return this;
    }

    public Restaurateur removeCommande(Commande commande) {
        this.commandes.remove(commande);
        commande.setRestaurateur(null);
        return this;
    }

    public void setCommandes(Set<Commande> commandes) {
        if (this.commandes != null) {
            this.commandes.forEach(i -> i.setRestaurateur(null));
        }
        if (commandes != null) {
            commandes.forEach(i -> i.setRestaurateur(this));
        }
        this.commandes = commandes;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Restaurateur)) {
            return false;
        }
        return id != null && id.equals(((Restaurateur) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Restaurateur{" +
            "id=" + getId() +
            ", nRestaurant=" + getnRestaurant() +
            ", adresseRestaurant='" + getAdresseRestaurant() + "'" +
            ", nomRestaurant='" + getNomRestaurant() + "'" +
            "}";
    }
}
