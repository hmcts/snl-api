package uk.gov.hmcts.reform.sandl.snlapi.security;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;
import uk.gov.hmcts.reform.sandl.snlapi.repositories.UserRepository;
import uk.gov.hmcts.reform.sandl.snlapi.security.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class CustomUserDetailsServiceTest {

    @InjectMocks
    CustomUserDetailsService cuds;

    @Mock
    UserRepository userRepository;

    @Before
    public void setup() {
        when(userRepository.findByUsername("officer1")).thenReturn(createOfficer1());
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

    private User createOfficer1() {
        User officer1 = new User();
        officer1.setUsername("officer1");
        officer1.setFullName("Listing Officer 1");
        officer1.setPassword("$2a$12$PjtPypb9NiQZuzEz2z5.Ge5vQSOJwO8TEI0KoGxnbOxbt5kGT.0Iy");
        officer1.setPasswordLastUpdated(LocalDateTime.now());
        officer1.setEmail("snl_officer1@hmcts.net");
        return officer1;
    }
}
