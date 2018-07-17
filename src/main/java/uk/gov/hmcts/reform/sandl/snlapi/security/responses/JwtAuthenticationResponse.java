package uk.gov.hmcts.reform.sandl.snlapi.security.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JwtAuthenticationResponse {
    private String accessToken;
    private String tokenType = "Bearer";

    public JwtAuthenticationResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
