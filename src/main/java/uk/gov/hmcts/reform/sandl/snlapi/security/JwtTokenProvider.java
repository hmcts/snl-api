package uk.gov.hmcts.reform.sandl.snlapi.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import uk.gov.hmcts.reform.sandl.snlapi.security.token.IUserToken;
import uk.gov.hmcts.reform.sandl.snlapi.security.token.InvalidUserToken;
import uk.gov.hmcts.reform.sandl.snlapi.security.token.TokenCreator;
import uk.gov.hmcts.reform.sandl.snlapi.security.token.UserToken;

import java.util.Date;

import static uk.gov.hmcts.reform.sandl.snlapi.security.token.IUserToken.MAX_EXPIRY_DATE;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
    private final UserTokenCreator tokenCreator;
    private final long jwtMaxExpirationInMs;
    private final String jwtSecret;

    JwtTokenProvider(
        @Value("${management.security.jwtSecret}") String jwtSecret,
        @Value("${management.security.jwtExpirationInMs}") long jwtExpirationInMs,
        @Value("${management.security.jwtMaxExpirationInMs}") long jwtMaxExpirationInMs
    ) {
        this.jwtSecret = jwtSecret;
        this.jwtMaxExpirationInMs = jwtMaxExpirationInMs;
        this.tokenCreator = new UserTokenCreator(jwtSecret, jwtExpirationInMs);
    }

    public String generateToken(Date maxExpiryDate) {
        return tokenCreator.createToken(maxExpiryDate);
    }

    public Date generateNewMaxExpiryDate() {
        return new Date(new Date().getTime() + jwtMaxExpirationInMs);
    }

    public IUserToken parseToken(String jwtToken) {
        if (!StringUtils.hasText(jwtToken)) {
            return new InvalidUserToken();
        }
        try {
            final Jws<Claims> claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwtToken);
            return new UserToken(claims);
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token", ex);
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token", ex);
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token", ex);
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.", ex);
        }
        return new InvalidUserToken();
    }

    class UserTokenCreator {
        private TokenCreator tokenCreator;

        UserTokenCreator(String secret, long expiration) {
            this.tokenCreator = new TokenCreator(secret, expiration);
        }

        String createToken(Date maxExpiryDate) {
            tokenCreator.addClaim("sub", tokenCreator.getCurrentUserName());
            tokenCreator.addClaim(MAX_EXPIRY_DATE, maxExpiryDate);
            return tokenCreator.createToken();
        }
    }
}
