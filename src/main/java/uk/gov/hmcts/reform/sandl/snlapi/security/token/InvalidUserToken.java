package uk.gov.hmcts.reform.sandl.snlapi.security.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import java.util.Date;
import java.util.UUID;

public class InvalidUserToken implements IUserToken {
    public InvalidUserToken() {
    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public Date getMaxExpiryDate() {
        return null;
    }

    @Override
    public String getUserId() {
        return null;
    }

    @Override
    public Jws<Claims> getClaims() {
        return null;
    }

    @Override
    public UUID getId() {
        return null;
    }
}
