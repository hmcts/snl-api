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
        usersToAdd.put("officer1", new User("officer1", "asd", "Officer 1",
            Arrays.asList("USER", "OFFICER")));
        usersToAdd.put("judge1", new User("judge1", "asd", "John Harris",
            Arrays.asList("USER", "JUDGE")));
        usersToAdd.put("admin", new User("admin", "asd", "Administrator man",
            Arrays.asList("USER", "ADMIN")));

        this.users = Collections.unmodifiableMap(usersToAdd);
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) {
        try {
            User user = users.get(usernameOrEmail);
            return UserPrincipal.create(user);
        } catch (NullPointerException ex) {
            throw new UsernameNotFoundException("User not found with username or email : " + usernameOrEmail, ex);
        }
    }
}

