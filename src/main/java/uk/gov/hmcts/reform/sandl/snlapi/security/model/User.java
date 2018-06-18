package uk.gov.hmcts.reform.sandl.snlapi.security.model;

import lombok.Data;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private String username;
    private String password;
    private String fullName;
    private Set<Role> roles = new HashSet<>();

    public User(String username, String password, String fullName, Collection<String> newRoles) {
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        newRoles.forEach(role -> this.roles.add(new Role(role)));
    }
}
