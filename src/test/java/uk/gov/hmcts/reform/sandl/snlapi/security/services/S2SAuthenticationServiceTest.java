package uk.gov.hmcts.reform.sandl.snlapi.security.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.impl.DefaultClaims;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.core.context.SecurityContextHolder.getContext;

@RunWith(SpringRunner.class)
public class S2SAuthenticationServiceTest {

    private static final String SECRET_EVENTS = "SecretE";
    private static final long DEFAULT_EXPIRY = 5000;
    private static final String SERVICE_NAME_SNL_API = "snl-api";
    private static final String USER_NAME_SNL_API = "anonymous";
    private S2SAuthenticationService s2SAuthenticationService;

    @Before
    public void setup() {
        getContext().setAuthentication(null);
        s2SAuthenticationService = new S2SAuthenticationService(SECRET_EVENTS, DEFAULT_EXPIRY);
    }

    @Test
    public void createRulesAuthenticationHeader_returnsHeaderWithNewlyCreatedToken() {
        HttpHeaders result = this.s2SAuthenticationService.createAuthenticationHeader();

        assertThat(result.containsKey(S2SAuthenticationService.HEADER_NAME)).isTrue();
        assertThat(result.getFirst(S2SAuthenticationService.HEADER_NAME))
            .contains(S2SAuthenticationService.HEADER_CONTENT_PREFIX);
    }

    @Test
    public void tokenCreator_createToken_forRules_containsProperFields() {
        final String token = s2SAuthenticationService.new S2StokenCreator(
            SECRET_EVENTS, DEFAULT_EXPIRY, SERVICE_NAME_SNL_API)
            .createToken();

        Claims claims = new DefaultClaims();
        boolean exceptionThrown = false;
        try {
            claims = Jwts.parser()
                .setSigningKey(SECRET_EVENTS)
                .parseClaimsJws(token)
                .getBody();
        } catch (SignatureException ex) {
            exceptionThrown = true;
        }
        assertThat(exceptionThrown).isFalse();

        final String serviceName = (String) claims.get("service");
        assertThat(SERVICE_NAME_SNL_API).isEqualTo(serviceName);

        final String userName = (String) claims.get("user");
        assertThat(USER_NAME_SNL_API).isEqualTo(userName);

        long millisDifference = claims.getExpiration().getTime() - claims.getIssuedAt().getTime();
        assertThat(DEFAULT_EXPIRY).isEqualTo(millisDifference);
    }

}
