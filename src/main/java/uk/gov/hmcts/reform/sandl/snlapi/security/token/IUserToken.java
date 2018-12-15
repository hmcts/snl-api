package uk.gov.hmcts.reform.sandl.snlapi.security.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import uk.gov.hmcts.reform.sandl.snlapi.repositories.UserRepository;

import java.util.Date;
import java.util.UUID;

public interface IUserToken {
    String MAX_EXPIRY_DATE = "maxExpiryDate";

    Date getMaxExpiryDate();

    String getUserId();

    Jws<Claims> getClaims();

    boolean isValid(UserRepository userRepository);

    UUID getId();
}
