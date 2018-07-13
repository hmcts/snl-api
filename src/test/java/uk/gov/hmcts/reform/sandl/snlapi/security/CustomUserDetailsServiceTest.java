package uk.gov.hmcts.reform.sandl.snlapi.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class CustomUserDetailsServiceTest {

    CustomUserDetailsService cuds;

    public CustomUserDetailsServiceTest() {
        this.cuds = new CustomUserDetailsService();
    }

    @Test
    public void loadUserByUsername_whenUserExistsItReturnsItsUserDetails() {
        String username = "officer1";

        UserDetails officer1 = cuds.loadUserByUsername(username);

        assertThat(officer1.getUsername()).isEqualTo(username);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void loadUserByUsername_whenUserDoesNotExistsItThrowsException() {
        String username = "_NON-EXISTENT_USERNAME_###";

        cuds.loadUserByUsername(username);
    }

}
