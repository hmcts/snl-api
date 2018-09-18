package uk.gov.hmcts.reform.sandl.snlapi.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import uk.gov.hmcts.reform.sandl.snlapi.security.model.User;
import uk.gov.hmcts.reform.sandl.snlapi.security.model.UserPrincipal;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@RunWith(SpringRunner.class)
public class JwtTokenProviderTest {

    public static final int MAX_EXPIRY_MS = 1000000;
    public static final String SECRET = "secret";
    public static final int JWT_EXPIRY_MS = 1000000;
    protected JwtTokenProvider jtp;

    protected String username = "username";
    protected String password = "password";
    protected String fullname = "fullname";

    public JwtTokenProviderTest() {
        this.jtp = new JwtTokenProvider(SECRET, JWT_EXPIRY_MS, MAX_EXPIRY_MS);

        Authentication auth = createAuth();
        getContext().setAuthentication(auth);
    }

    @Test
    // Test that integrates generating token and retrieving info from it
    public void generateToken_getUserIdFromJwt_UsernameIsRetrievableFromGeneratedToken() {
        String token = jtp.generateToken(jtp.generateNewMaxExpiryDate());
        String actualUsername = jtp.parseToken(token).getUserId();

        assertThat(actualUsername).isEqualTo(username);
    }

    @Test
    public void generateToken_getMaxExpiryDateFromJwt_UsernameIsRetrievableFromGeneratedToken() {
        Date expected = new Date(new Date().getTime() + MAX_EXPIRY_MS);
        String token = jtp.generateToken(jtp.generateNewMaxExpiryDate());

        Date maxExpiryDate = jtp.parseToken(token).getMaxExpiryDate();

        // full aware that it's not precise, but easier to add and still checks our solution
        assertThat(maxExpiryDate).isAfterOrEqualsTo(expected);
    }

    @Test
    public void generateToken_getMaxExpiryDateFromJwt_whenFieldIsNotGiven_returnsNull() {
        String token = jtp.generateToken(null);

        Date maxExpiryDate = jtp.parseToken(token).getMaxExpiryDate();
        assertThat(maxExpiryDate).isNull();
    }

    @Test
    public void validateToken_withMaxExpiryDateInPast_shouldReturnFalse() {
        Date dateInPast = new Date(new Date().getTime() - MAX_EXPIRY_MS - 1);

        String token = jtp.generateToken(dateInPast);

        boolean isTokenValid = jtp.parseToken(token).isValid();

        assertThat(isTokenValid).isFalse();
    }

    @Test
    public void validateToken_withMaxExpiryDateInFuture_shouldReturnFalse() {
        Date dateInFuture = new Date(new Date().getTime() + MAX_EXPIRY_MS);
        String token = jtp.generateToken(dateInFuture);

        boolean isTokenValid = jtp.parseToken(token).isValid();

        assertThat(isTokenValid).isTrue();
    }

    @Test
    public void validateToken_withoutMaxExpiryDate_shouldReturnFalse() {
        String token = jtp.generateToken(null);
        boolean isTokenValid =jtp.parseToken(token).isValid();

        assertThat(isTokenValid).isFalse();
    }


    private Authentication createAuth() {
        return createAuth(createUser());
    }

    private Authentication createAuth(User user) {
        return new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public Object getPrincipal() {
                return UserPrincipal.create(user);
            }

            @Override
            public boolean isAuthenticated() {
                return true;
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

            }

            @Override
            public String getName() {
                return username;
            }
        };
    }

    private User createUser() {
        return new User(username, password, fullname, Collections.emptyList());
    }

}
