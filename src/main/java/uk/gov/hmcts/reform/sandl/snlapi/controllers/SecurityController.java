package uk.gov.hmcts.reform.sandl.snlapi.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/security")
public class SecurityController {

    @GetMapping("/user")
    public UserDetails user(@AuthenticationPrincipal UserDetails user) {
        return user;
    }
}
