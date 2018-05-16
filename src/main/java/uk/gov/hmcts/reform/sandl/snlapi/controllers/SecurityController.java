package uk.gov.hmcts.reform.sandl.snlapi.controllers;

import javafx.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.sandl.snlapi.model.UserSessionDetails;

@RestController()
@RequestMapping("/security")
public class SecurityController {

    @GetMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserSessionDetails user(@AuthenticationPrincipal UserDetails user) {
        return new UserSessionDetails(user);
    }

    @GetMapping(value = "/csrftoken", produces = MediaType.APPLICATION_JSON_VALUE)
    public Pair token(@CookieValue("XSRF-TOKEN") String token) {
        return new Pair("token", token);
    }
}
