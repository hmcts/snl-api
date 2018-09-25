package uk.gov.hmcts.reform.sandl.snlapi.security.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import java.util.Date;

public class InvalidUserToken implements IUserToken {
    public InvalidUserToken() {
    }

    public boolean isValid() {
        return false;
    }

    public Date getMaxExpiryDate() {
        return null;
    }

    public String getUserId() {
        return null;
    }

    public Jws<Claims> getClaims() {
        return null;
    }
}
