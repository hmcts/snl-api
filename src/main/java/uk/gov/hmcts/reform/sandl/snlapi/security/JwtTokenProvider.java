package uk.gov.hmcts.reform.sandl.snlapi.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.sandl.snlapi.security.model.UserPrincipal;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
    public static final String MAX_EXPIRY_DATE = "maxExpiryDate";

    @Value("${management.security.jwtSecret}")
    private String jwtSecret;

    @Value("${management.security.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    @Value("${management.security.jwtMaxExpirationInMs}")
    private int jwtMaxExpirationInMs;

    public String generateToken(Authentication authentication, Date maxExpiryDate) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);
        Map<String, Object> claims = new HashMap<>();
        claims.put(MAX_EXPIRY_DATE, maxExpiryDate);

        return
            Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUserIdFromJwt(String token) {
        Claims claims = Jwts.parser()
            .setSigningKey(jwtSecret)
            .parseClaimsJws(token)
            .getBody();

        return claims.getSubject();
    }

    public Date getMaxExpiryDateFromJwt(String token) {
        Claims claims = Jwts.parser()
            .setSigningKey(jwtSecret)
            .parseClaimsJws(token)
            .getBody();

        return new Date((Long) claims.get(MAX_EXPIRY_DATE));
    }

    public Date generateNewMaxExpiryDate() {
        return new Date(new Date().getTime() + jwtMaxExpirationInMs);
    }

    public boolean validateToken(String authToken) {
        try {
            final Jws<Claims> claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            final Date maxExpiryDate = claims.getBody().get(MAX_EXPIRY_DATE, Date.class);
            if (new Date().compareTo(maxExpiryDate) > 0) {
                return false;
            }
            return true;
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token", ex);
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token", ex);
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token", ex);
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.", ex);
        }
        return false;
    }
}
