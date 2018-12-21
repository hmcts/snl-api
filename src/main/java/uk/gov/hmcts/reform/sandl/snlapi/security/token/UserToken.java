package uk.gov.hmcts.reform.sandl.snlapi.security.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import java.util.Date;
import java.util.UUID;

public class UserToken implements IUserToken {

    private Jws<Claims> claims;

    public UserToken(Jws<Claims> claims) {
        this.claims = claims;
    }

    @Override
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

    @Override
    public Date getMaxExpiryDate() {
        return claims.getBody().get(MAX_EXPIRY_DATE, Date.class);
    }

    @Override
    public String getUserId() {
        return claims.getBody().getSubject();
    }

    @Override
    public Jws<Claims> getClaims() {
        return claims;
    }

    @Override
    public UUID getId() {
        return UUID.fromString(claims.getBody().getId());
    }
}
