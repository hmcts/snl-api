package uk.gov.hmcts.reform.sandl.snlapi.security.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@Slf4j
@Service
public class S2SAuthenticationService {

    public static final String HEADER_NAME = "Authorization";
    public static final String HEADER_CONTENT_PREFIX = "Bearer ";
    private final TokenCreator tokenCreator;


    public S2SAuthenticationService(
        @Value("${management.security.events.jwtSecret}")  String jwtSecret,
        @Value("${management.security.events.jwtExpirationInMs}") long jwtExpirationInMs
    ) {
        this.tokenCreator = new TokenCreator(jwtSecret, jwtExpirationInMs, "snl-api");
    }

    public HttpHeaders createAuthenticationHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HEADER_NAME, HEADER_CONTENT_PREFIX + this.tokenCreator.createToken());
        return headers;
    }

    class TokenCreator {
        private String secret;
        private long expiration;
        private String serviceName;

        TokenCreator(String secret, long expiration, String serviceName) {
            this.secret = secret;
            this.expiration = expiration;
            this.serviceName = serviceName;
        }

        String createToken() {
            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + expiration);

            String userName = findCurrentUser();

            return Jwts.builder()
                .claim("service", serviceName)
                .claim("user", userName)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
        }

        private String findCurrentUser() {
            if (getContext() != null && getContext().getAuthentication() != null
                && getContext().getAuthentication().isAuthenticated()) {
                return getContext().getAuthentication().getName();
            } else {
                return "anonymous";
            }
        }
    }
}
