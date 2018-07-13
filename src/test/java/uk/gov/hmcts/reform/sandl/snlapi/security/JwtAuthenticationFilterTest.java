package uk.gov.hmcts.reform.sandl.snlapi.security;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import java.io.IOException;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class JwtAuthenticationFilterTest {
    private static final String BEARER = "Bearer ";
    private static final String TOKEN_VALUE = "eyJhbGciOiJIUzUxMiJ9" +
        ".eyJzdWIiOiJvZmZpY2VyMSIsImlhdCI6MTUzMTQwMjI1NiwiZXhwIjoxNTMxNDA0MDU2fQ.LMPw_wMySaTgM3WgNcpI0pnvohiePTj0UMujKtZ5IPNUYtSCO5_Z7Gq7cowANfKRZw2AIiB5nPfuo8y23IirSw";
    private static final String INVALID_TOKEN = "invalid_token";

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private FilterChain chain;

    @MockBean
    private JwtTokenProvider tokenProvider;
    @SpyBean
    private CustomUserDetailsService userDetailsService;

    @TestConfiguration
    static class JwtAuthenticationFilterTestContextConfiguration {
        @Bean
        public JwtAuthenticationFilter jwtAuthenticationFilter() {
            return new JwtAuthenticationFilter();
        }
    }

    @Before
    public void setup() {
        this.request = new MockHttpServletRequest();
        this.response = new MockHttpServletResponse();
        this.chain = mock(FilterChain.class);
    }

    @Test
    public void doFilterInternal_withValidTokenAsProperUser_shouldAuthenticate() throws ServletException, IOException {
        when(tokenProvider.validateToken(TOKEN_VALUE)).thenReturn(true);
        when(tokenProvider.getUserIdFromJwt(TOKEN_VALUE)).thenReturn("officer1");

        request.addHeader("Authorization", BEARER + TOKEN_VALUE);

        jwtAuthenticationFilter.doFilterInternal(request, response, chain);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        assertTrue(auth.isAuthenticated());
        verify(chain, times(1)).doFilter(request, response);
    }

    @Test
    public void doFilterInternal_withInvalidToken_shouldNotAuthenticate() throws ServletException, IOException {
        when(tokenProvider.validateToken(INVALID_TOKEN)).thenReturn(false);

        request.addHeader("Authorization", BEARER + INVALID_TOKEN);

        jwtAuthenticationFilter.doFilterInternal(request, response, chain);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        assertNull(auth);
        verify(chain, times(1)).doFilter(request, response);
    }

    @Test
    public void doFilterInternal_validTokenButNotUser_shouldNotAuthenticate() throws ServletException, IOException {
        when(tokenProvider.validateToken(TOKEN_VALUE)).thenReturn(true);
        when(tokenProvider.getUserIdFromJwt(TOKEN_VALUE)).thenReturn("definitely not a user");

        request.addHeader("Authorization", BEARER + TOKEN_VALUE);

        jwtAuthenticationFilter.doFilterInternal(request, response, chain);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        assertNull(auth);
        verify(chain, times(1)).doFilter(request, response);
    }
}
