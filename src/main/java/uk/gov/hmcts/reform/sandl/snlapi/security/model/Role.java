package uk.gov.hmcts.reform.sandl.snlapi.security.model;

import lombok.Data;

import java.util.Locale;

@Data
public class Role {
    private String name;

    public Role(String name) {
        if (name == null) {
            this.name = "EMPTY";
        } else if (!name.startsWith("ROLE_")) {
            this.name = "ROLE_" + name.toUpperCase(Locale.ENGLISH);
        } else {
            this.name = name.toUpperCase(Locale.ENGLISH);
        }
    }
}
