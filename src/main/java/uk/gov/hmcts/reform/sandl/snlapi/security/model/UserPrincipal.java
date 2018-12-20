package uk.gov.hmcts.reform.sandl.snlapi.security.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@ToString
public class UserPrincipal implements UserDetails {

    @NonNull
    private final String fullName;

    @NonNull
    private final String username;

    @NonNull
    @JsonIgnore
    private final String password;

    @NonNull
    private final Collection<? extends GrantedAuthority> authorities;

    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;

    public static UserPrincipal create(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream().map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

        return new UserPrincipal(user.getFullName(),
                                 user.getUsername(),
                                 user.getPassword(),
                                 authorities,
                                 true,
                                 true,
                                 !user.isResetRequired(),
                                 user.isEnabled());
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
