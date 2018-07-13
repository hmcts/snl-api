package uk.gov.hmcts.reform.sandl.snlapi.security;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
public class JwtAuthenticationFilterTest {

    private JwtAuthenticationFilter jwtAuthenticationFilter;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private FilterChain chain;


    @Before
    public void setup() {
        this.jwtAuthenticationFilter = new JwtAuthenticationFilter();
        this.request = new MockHttpServletRequest();
        this.response = new MockHttpServletResponse();
        this.chain = mock(FilterChain.class);
    }

    @Test
    public void doFilterInternal_shouldPassChainFilter() throws ServletException, IOException {

        request.addHeader("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJvZmZpY2VyMSIsImlhdCI6MTUzMTQwMjI1NiwiZXhwIjoxNTMxNDA0MDU2fQ.LMPw_wMySaTgM3WgNcpI0pnvohiePTj0UMujKtZ5IPNUYtSCO5_Z7Gq7cowANfKRZw2AIiB5nPfuo8y23IirSw");

        jwtAuthenticationFilter.doFilterInternal(request, response, chain);
        verify(chain, times(1)).doFilter(request, response);
    }
}
