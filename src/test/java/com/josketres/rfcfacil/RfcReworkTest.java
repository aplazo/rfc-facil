package com.josketres.rfcfacil;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class RfcReworkTest {

    @Test
    public void should_not_throw_for_names_with_special_characters() {
        Rfc rfc = new Rfc.Builder()
                .name("Madeline 💗")
                .firstLastName("Tamayo")
                .secondLastName("Hernandez")
                .birthday(1, 3, 2000)
                .build();
        assertThat(rfc.toString(), equalTo("TAHM0003012G2"));
        Rfc rfc_b = new Rfc.Builder()
                .name("Jesus Antonio")
                .firstLastName("López")
                .secondLastName("Ventura")
                .birthday(17, 8, 2004)
                .build();
        assertThat(rfc_b.toString(), equalTo("LOVJ040817T61"));
        Rfc rfc_c = new Rfc.Builder()
                .name("María De La Luz")
                .firstLastName("Ku")
                .secondLastName("Sarabia")
                .birthday(24, 5, 1970)
                .build();
        assertThat(rfc_c.toString(), equalTo("KSLU700524TZ0"));
        Rfc rfc_d = new Rfc.Builder()
                .name("María De La Luz")
                .firstLastName(".")
                .secondLastName("Sarabia")
                .birthday(24, 5, 1970)
                .build();
        assertThat(rfc_d.toString(), equalTo("SALU700524LJ7"));
        Rfc rfc_e = new Rfc.Builder()
                .name("Alberto")
                .firstLastName("Ñando")
                .secondLastName("Rodríguez")
                .birthday(24, 5, 1970)
                .build();
        assertThat(rfc_e.toString(), equalTo("NARA7005248R8"));
        Rfc rfc_f = new Rfc.Builder()
                .name("ᵀᵃⁿⁱᵃ Gᵘᵃᵈᵃˡᵘᵖᵉ Jᵒˢᵉᶠⁱⁿᵃ")
                .firstLastName("Lᵉᵈᵉˢᵐᵃ")
                .secondLastName("Sᵉʳⁿᵃ")
                .birthday(24, 5, 1970)
                .build();
        assertThat(rfc_f.toString(), equalTo("LSGJ7005249K4"));
    }
}
