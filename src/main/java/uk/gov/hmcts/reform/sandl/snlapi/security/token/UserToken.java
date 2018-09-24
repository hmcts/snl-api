package uk.gov.hmcts.reform.sandl.snlapi.security.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import java.util.Date;

public class UserToken implements IUserToken {
    private Jws<Claims> claims;

    public UserToken(Jws<Claims> claims) {
        this.claims = claims;
    }

    public boolean isValid() {
        final Date maxExpiryDate = getMaxExpiryDate();
        if (maxExpiryDate == null) {
            return false;
        }
        if (new Date().compareTo(maxExpiryDate) > 0) {
            return false;
        }
        return true;
    }

    public Date getMaxExpiryDate() {
        return claims.getBody().get(MAX_EXPIRY_DATE, Date.class);
    }

    public String getUserId() {
        return claims.getBody().getSubject();
    }

    public Jws<Claims> getClaims() {
        return claims;
    }
}
