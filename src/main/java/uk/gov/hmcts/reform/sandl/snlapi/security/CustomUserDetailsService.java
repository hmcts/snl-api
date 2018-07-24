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

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final Map<String, User> users;

    CustomUserDetailsService() {
        Map<String, User> usersToAdd = new HashMap<>();
        for (int i = 1; i <= 1000; i++) {
            usersToAdd.put("officer" + i, new User("officer" + i, "asd", "Officer " + i,
                Arrays.asList("USER", "OFFICER")));
        }
        usersToAdd.put("judge1", new User("judge1", "asd", "John Harris",
            Arrays.asList("USER", "JUDGE")));
        usersToAdd.put("admin", new User("admin", "asd", "Administrator man",
            Arrays.asList("USER", "ADMIN")));

        this.users = Collections.unmodifiableMap(usersToAdd);
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

