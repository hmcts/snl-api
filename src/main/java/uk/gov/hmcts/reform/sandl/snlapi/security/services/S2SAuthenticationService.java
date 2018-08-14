package uk.gov.hmcts.reform.sandl.snlapi.security.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.Date;

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

            return Jwts.builder()
                .claim("service", serviceName)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
        }
    }
}
