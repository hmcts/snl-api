package uk.gov.hmcts.reform.sandl.snlapi.security;

import uk.gov.hmcts.reform.sandl.snlapi.repositories.UserRepository;
import uk.gov.hmcts.reform.sandl.snlapi.security.model.User;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;

public final class MockUserRepository {

    private MockUserRepository() {}

    public static void setupMocks(UserRepository userRepository) {
        when(userRepository.findByUsername("officer1")).thenReturn(createOfficer1());
    }

    private static User createOfficer1() {
        User officer1 = new User();
        officer1.setUsername("officer1");
        officer1.setFullName("Listing Officer 1");
        officer1.setPassword("$2a$12$PjtPypb9NiQZuzEz2z5.Ge5vQSOJwO8TEI0KoGxnbOxbt5kGT.0Iy");
        officer1.setPasswordLastUpdated(LocalDateTime.now());
        officer1.setEmail("snl_officer1@hmcts.net");
        return officer1;
    }
}
