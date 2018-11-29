package uk.gov.hmcts.reform.sandl.snlapi.security.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class UserPrincipal implements UserDetails {

    @NonNull
    private String fullName;

    @NonNull
    private String username;

    @NonNull
    @JsonIgnore
    private String password;

    @NonNull
    private Collection<? extends GrantedAuthority> authorities;

    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired;
    private boolean enabled = true;

    public UserPrincipal(@NonNull String fullName,
                         @NonNull String username,
                         @NonNull String password,
                         @NonNull Collection<? extends GrantedAuthority> authorities,
                         boolean credentialsNonExpired) {
        this.fullName = fullName;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public static UserPrincipal create(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream().map(role ->
            new SimpleGrantedAuthority(role)
        ).collect(Collectors.toList());

        return new UserPrincipal(
            user.getFullName(),
            user.getUsername(),
            user.getPassword(),
            authorities,
            !user.isResetRequired()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserPrincipal that = (UserPrincipal) o;
        return Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

}
