package uk.gov.hmcts.reform.sandl.snlapi.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Data
public class UserSessionDetails {
    private Collection<? extends GrantedAuthority> authorities;
    private String username;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;

    public UserSessionDetails(UserDetails user) {
        this.authorities = user.getAuthorities();
        this.username = user.getUsername();
        this.accountNonExpired = user.isAccountNonExpired();
        this.accountNonLocked = user.isAccountNonLocked();
        this.credentialsNonExpired = user.isCredentialsNonExpired();
        this.enabled = user.isEnabled();
    }
}
