package com.coopcycle.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.coopcycle.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommandeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Commande.class);
        Commande commande1 = new Commande();
        commande1.setId(1L);
        Commande commande2 = new Commande();
        commande2.setId(commande1.getId());
        assertThat(commande1).isEqualTo(commande2);
        commande2.setId(2L);
        assertThat(commande1).isNotEqualTo(commande2);
        commande1.setId(null);
        assertThat(commande1).isNotEqualTo(commande2);
    }
}
