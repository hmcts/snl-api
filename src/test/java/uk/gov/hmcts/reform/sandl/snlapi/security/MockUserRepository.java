package uk.gov.hmcts.reform.sandl.snlapi.security;

import uk.gov.hmcts.reform.sandl.snlapi.repositories.UserRepository;
import uk.gov.hmcts.reform.sandl.snlapi.security.model.User;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;

public final class MockUserRepository {

    private static final String ENCODED_ASD_PASSWORD =
        "fa2fbd666a080874b7d83e611aa1a4e67f8c010fef1b19d7725725aa74f177a324b6c033e1abaf9e";

    private MockUserRepository() {}

    public static void setupMocks(UserRepository userRepository) {
        when(userRepository.findByUsername("officer1")).thenReturn(createOfficer1());
    }

    private static User createOfficer1() {
        User officer1 = new User();
        officer1.setUsername("officer1");
        officer1.setFullName("Listing Officer 1");
        officer1.setPassword(ENCODED_ASD_PASSWORD);
        officer1.setPasswordLastUpdated(LocalDateTime.now());
        officer1.setEmail("snl_officer1@hmcts.net");
        return officer1;
    }
}
