package uk.gov.hmcts.reform.sandl.snlapi.security.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import java.util.Date;

public interface IUserToken {
    String MAX_EXPIRY_DATE = "maxExpiryDate";

    Date getMaxExpiryDate();

    String getUserId();

    Jws<Claims> getClaims();

    boolean isValid();
}
