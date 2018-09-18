package uk.gov.hmcts.reform.sandl.snlapi.security.token;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;

public class TokenCreator {
    private String secret;
    private long expiration;
    private Map<String, Object> claims;

    public TokenCreator(String secret, long expiration) {
        this.secret = secret;
        this.expiration = expiration;
        this.claims = new HashMap<>();
    }

    public void addClaim(String key, Object value) {
        if (key != null && value != null) {
            this.claims.put(key, value);
        }
    }

    public String createToken() {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        JwtBuilder builder = Jwts.builder();
        builder.addClaims(claims);
        return builder
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(SignatureAlgorithm.HS512, secret)
            .compact();
    }

    public String getCurrentUserName() {
        if (getContext() != null && getContext().getAuthentication() != null
            && getContext().getAuthentication().isAuthenticated()) {
            return getContext().getAuthentication().getName();
        } else {
            return "anonymous";
        }
    }
}
