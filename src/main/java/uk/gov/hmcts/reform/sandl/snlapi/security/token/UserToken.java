package uk.gov.hmcts.reform.sandl.snlapi.security.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.NonNull;
import uk.gov.hmcts.reform.sandl.snlapi.repositories.UserRepository;
import uk.gov.hmcts.reform.sandl.snlapi.security.model.User;

import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class UserToken implements IUserToken {

    private Jws<Claims> claims;

    public UserToken(Jws<Claims> claims) {
        this.claims = claims;
    }

    @Override
    public boolean isValid(@NonNull UserRepository userRepository) {
        final Date maxExpiryDate = getMaxExpiryDate();
        if (maxExpiryDate == null) {
            return false;
        }
        if (new Date().compareTo(maxExpiryDate) > 0) {
            return false;
        }
        String userId = getUserId();
        if (userId == null) {
            return false;
        }
        User user = userRepository.findByUsername(userId);
        if (user == null) {
            return false;
        }
        UUID id = getId();
        Set<UUID> userTokenIds = user.getTokens().stream().map(User.Token::getId).collect(Collectors.toSet());
        if (!userTokenIds.contains(id)) {
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
