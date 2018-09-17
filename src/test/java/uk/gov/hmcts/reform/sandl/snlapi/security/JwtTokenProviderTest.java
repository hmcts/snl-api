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

@RunWith(SpringRunner.class)
public class JwtTokenProviderTest {

    public static final int MAX_EXPIRY_MS = 1000000;
    protected JwtTokenProvider jtp;

    protected String username = "username";
    protected String password = "password";
    protected String fullname = "fullname";

    public JwtTokenProviderTest() {
        this.jtp = new JwtTokenProvider();

        ReflectionTestUtils.setField(jtp, "jwtSecret", "secret");
        ReflectionTestUtils.setField(jtp, "jwtExpirationInMs", 100000);
        ReflectionTestUtils.setField(jtp, "jwtMaxExpirationInMs", MAX_EXPIRY_MS);
    }

    @Test
    // Test that integrates generating token and retrieving info from it
    public void generateToken_getUserIdFromJwt_UsernameIsRetrievableFromGeneratedToken() {
        Authentication auth = createAuth();

        String token = jtp.generateToken(auth, jtp.generateNewMaxExpiryDate());

        String actualUsername = jtp.getUserIdFromJwt(token);

        assertThat(actualUsername).isEqualTo(username);
    }

    @Test
    public void generateToken_getMaxExpiryDateFromJwt_UsernameIsRetrievableFromGeneratedToken() {
        Authentication auth = createAuth();

        Date expected = new Date(new Date().getTime() + MAX_EXPIRY_MS);
        String token = jtp.generateToken(auth, jtp.generateNewMaxExpiryDate());

        Date maxExpiryDate = jtp.getMaxExpiryDateFromJwt(token);

        // full aware that it's not precise, but easier to add and still checks our solution
        assertThat(maxExpiryDate).isAfterOrEqualsTo(expected);
    }

    @Test(expected = RuntimeException.class)
    public void generateToken_getMaxExpiryDateFromJwt_whenFieldIsBlank_throwsException() {
        Authentication auth = createAuth();

        String token = jtp.generateToken(auth, null);

        Date maxExpiryDate = jtp.getMaxExpiryDateFromJwt(token);
    }

    @Test
    public void validateToken_withMaxExpiryDateInPast_shouldReturnFalse() {
        Authentication auth = createAuth();
        Date dateInPast = new Date(new Date().getTime() - MAX_EXPIRY_MS - 1);

        String token = jtp.generateToken(auth, dateInPast);

        boolean isTokenValid = jtp.validateToken(token);

        assertThat(isTokenValid).isFalse();
    }

    @Test
    public void validateToken_withMaxExpiryDateInFuture_shouldReturnFalse() {
        Authentication auth = createAuth();
        Date dateInFuture = new Date(new Date().getTime() + MAX_EXPIRY_MS);

        String token = jtp.generateToken(auth, dateInFuture);

        boolean isTokenValid = jtp.validateToken(token);

        assertThat(isTokenValid).isTrue();
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
                return false;
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

            }

            @Override
            public String getName() {
                return null;
            }
        };
    }

    private User createUser() {
        return new User(username, password, fullname, Collections.emptyList());
    }

}
