package uk.gov.hmcts.reform.sandl.snlapi.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.sandl.snlapi.security.model.User;
import uk.gov.hmcts.reform.sandl.snlapi.security.model.UserPrincipal;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("NonStaticInitializer")
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static Map<String, User> users;

    static {
        users = Collections.unmodifiableMap(
            new HashMap<String, User>() {
                {
                    put("officer1", new User("officer1", "asd", "Officer 1",
                        Arrays.asList("USER", "OFFICER")));
                    put("judge1", new User("judge1", "asd", "John Harris",
                        Arrays.asList("USER", "JUDGE")));
                    put("admin", new User("admin", "asd", "Administrator man",
                        Arrays.asList("USER", "ADMIN")));
                }
            });
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail)
        throws UsernameNotFoundException {
        try {
            User user = users.get(usernameOrEmail);
            return UserPrincipal.create(user);
        } catch (NullPointerException ex) {
            new UsernameNotFoundException("User not found with username or email : " + usernameOrEmail);
        }
        return null;
    }
}

