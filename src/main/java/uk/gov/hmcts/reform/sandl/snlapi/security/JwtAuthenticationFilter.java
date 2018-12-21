package uk.gov.hmcts.reform.sandl.snlapi.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import uk.gov.hmcts.reform.sandl.snlapi.security.token.IUserToken;

import java.io.IOException;
import java.util.Date;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static uk.gov.hmcts.reform.sandl.snlapi.security.services.S2SAuthenticationService.HEADER_CONTENT_PREFIX;
import static uk.gov.hmcts.reform.sandl.snlapi.security.services.S2SAuthenticationService.HEADER_NAME;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private CustomUserDetailsService userDetailsService;
    @Value("${management.security.newTokenHeaderName}")
    private String newTokenHeaderName;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);
            IUserToken userToken = tokenProvider.parseToken(jwt);

            if (userToken.isValid() && tokenProvider.isTokenRecognisedForUser(userToken)) {
                String userId = userToken.getUserId();

                UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);

                addRefreshTokenToResponse(response, userToken.getMaxExpiryDate());
            }
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(request, response);
    }

    private void addRefreshTokenToResponse(HttpServletResponse response, Date maxExpiryDate) {
        response.addHeader(newTokenHeaderName, createJwtForResponse(
            tokenProvider.generateToken(maxExpiryDate)
        ));
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(HEADER_NAME);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(HEADER_CONTENT_PREFIX)) {
            return bearerToken.substring(HEADER_CONTENT_PREFIX.length(), bearerToken.length());
        }
        return null;
    }

    private String createJwtForResponse(String pureJwt) {
        return String.format("%s%s", HEADER_CONTENT_PREFIX, pureJwt);
    }
}
