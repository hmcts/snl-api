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

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class JwtTokenProviderTest {

    protected JwtTokenProvider jtp;

    protected String username = "username";
    protected String password = "password";
    protected String fullname = "fullname";

    public JwtTokenProviderTest() {
        this.jtp = new JwtTokenProvider();

        ReflectionTestUtils.setField(jtp, "jwtSecret", "secret");
        ReflectionTestUtils.setField(jtp, "jwtExpirationInMs", 100000);
    }

    @Test
    // Test that integrates generating token and retrieving info from it
    public void generateToken_getUserIdFromJwt_UsernameIsRetrievableFromGeneratedToken() {
        Authentication auth = createAuth();

        String token = jtp.generateToken(auth);

        String actualUsername = jtp.getUserIdFromJwt(token);

        assertThat(actualUsername).isEqualTo(username);
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
