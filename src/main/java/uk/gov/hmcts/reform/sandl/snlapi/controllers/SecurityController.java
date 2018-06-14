package uk.gov.hmcts.reform.sandl.snlapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.sandl.snlapi.model.UserSessionDetails;
import uk.gov.hmcts.reform.sandl.snlapi.security.CurrentUser;
import uk.gov.hmcts.reform.sandl.snlapi.security.CustomUserDetailsService;
import uk.gov.hmcts.reform.sandl.snlapi.security.JwtTokenProvider;
import uk.gov.hmcts.reform.sandl.snlapi.security.requests.LoginRequest;
import uk.gov.hmcts.reform.sandl.snlapi.security.responses.JwtAuthenticationResponse;

import javax.validation.Valid;

@RestController()
@RequestMapping("/security")
public class SecurityController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider tokenProvider;

    @Autowired
    CustomUserDetailsService cudService;

    @GetMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserSessionDetails user(@CurrentUser UserDetails user) {
        return new UserSessionDetails(user);
    }

    @GetMapping(value = "/csrftoken", produces = MediaType.APPLICATION_JSON_VALUE)
    public String[] token(@CookieValue("XSRF-TOKEN") String token) {
        return new String[] { token };
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
            )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

}
