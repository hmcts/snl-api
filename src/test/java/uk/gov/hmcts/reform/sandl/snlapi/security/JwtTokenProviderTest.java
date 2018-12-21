package uk.gov.hmcts.reform.sandl.snlapi.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.hmcts.reform.sandl.snlapi.repositories.UserRepository;
import uk.gov.hmcts.reform.sandl.snlapi.security.model.User;
import uk.gov.hmcts.reform.sandl.snlapi.security.model.UserPrincipal;
import uk.gov.hmcts.reform.sandl.snlapi.security.token.IUserToken;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.when;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@RunWith(SpringRunner.class)
public class JwtTokenProviderTest {

    public static final int MAX_EXPIRY_MS = 1000000;
    public static final String SECRET = "secret";
    public static final int JWT_EXPIRY_MS = 1000000;

    @InjectMocks
    protected JwtTokenProvider jtp;

    protected String username = "username";
    // bcrypt hash of "password"
    protected String password = "$2y$12$CkfWE9BYsfEO/gtx4NiXHuifYFXnaCTbMVPqJQgfqGn0NZoucN7S6";
    protected String fullname = "fullname";
    protected LocalDateTime passwordLastUpdated = LocalDateTime.now();

    @Mock
    UserRepository userRepository;

    public JwtTokenProviderTest() {
        this.jtp = new JwtTokenProvider(SECRET, JWT_EXPIRY_MS, MAX_EXPIRY_MS);

        Authentication auth = createAuth();
        getContext().setAuthentication(auth);
    }

    @Test
    // Test that integrates generating token and retrieving info from it
    public void generateToken_getUserIdFromJwt_UsernameIsRetrievableFromGeneratedToken() {
        when(userRepository.findByUsername(username)).thenReturn(getUser());
        String token = jtp.generateToken(jtp.generateNewMaxExpiryDate());
        String actualUsername = jtp.parseToken(token).getUserId();

        assertThat(actualUsername).isEqualTo(username);
    }

    @Test
    public void generateToken_getMaxExpiryDateFromJwt_UsernameIsRetrievableFromGeneratedToken() {
        Date expected = new Date(new Date().getTime() + MAX_EXPIRY_MS);
        when(userRepository.findByUsername(username)).thenReturn(getUser());
        String token = jtp.generateToken(jtp.generateNewMaxExpiryDate());

        Date maxExpiryDate = jtp.parseToken(token).getMaxExpiryDate();

        // full aware that it's not precise, but easier to add and still checks our solution
        assertThat(maxExpiryDate).isAfterOrEqualsTo(expected);
    }

    @Test
    public void validateToken_withMaxExpiryDateInPast_shouldReturnFalse() {
        Date dateInPast = new Date(new Date().getTime() - MAX_EXPIRY_MS - 1);
        assertThat(validateTokenWithDate(dateInPast)).isFalse();
    }

    @Test
    public void generateToken_withNullDate_shouldThrowNullPointerException() {
        Throwable thrown = catchThrowable(() -> jtp.generateToken(null));
        assertThat(thrown).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void validateToken_withMaxExpiryDateInFuture_shouldReturnTrue() {
        Date dateInFuture = new Date(new Date().getTime() + MAX_EXPIRY_MS);
        assertThat(validateTokenWithDate(dateInFuture)).isTrue();
    }

    private User getUser() {
        User user = new User();
        user.setId(1);
        user.setUsername(username);
        user.setPassword(password);
        user.setPasswordLastUpdated(passwordLastUpdated);
        user.setFullName(fullname);
        user.setResetRequired(false);
        return user;
    }

    private boolean validateTokenWithDate(Date date) {
        when(userRepository.findByUsername(username)).thenReturn(getUser());
        String tokenString = jtp.generateToken(date);
        IUserToken token = jtp.parseToken(tokenString);
        boolean isTokenValid = token.isValid();
        return isTokenValid;
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
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setPasswordLastUpdated(passwordLastUpdated);
        user.setFullName(fullname);
        return user;
    }

}
