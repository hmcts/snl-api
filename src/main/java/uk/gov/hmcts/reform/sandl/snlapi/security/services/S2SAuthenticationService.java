package uk.gov.hmcts.reform.sandl.snlapi.security.services;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import uk.gov.hmcts.reform.sandl.snlapi.security.token.TokenCreator;

import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@Slf4j
@Service
public class S2SAuthenticationService {

    public static final String HEADER_NAME = "Authorization";
    public static final String HEADER_CONTENT_PREFIX = "Bearer ";
    private final S2StokenCreator tokenCreator;


    public S2SAuthenticationService(
        @Value("${management.security.events.jwtSecret}")  String jwtSecret,
        @Value("${management.security.events.jwtExpirationInMs}") long jwtExpirationInMs
    ) {
        this.tokenCreator = new S2StokenCreator(jwtSecret, jwtExpirationInMs, "snl-api");
    }

    public HttpHeaders createAuthenticationHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HEADER_NAME, HEADER_CONTENT_PREFIX + this.tokenCreator.createToken());
        return headers;
    }

    class S2StokenCreator {
        private TokenCreator tokenCreator;

        S2StokenCreator(String secret, long expiration, String serviceName) {
            this.tokenCreator = new TokenCreator(secret, expiration);
            tokenCreator.addClaim("service", serviceName);
        }

        String createToken() {
            tokenCreator.addClaim("user", tokenCreator.getCurrentUserName());
            return tokenCreator.createToken();
        }

    }
}
