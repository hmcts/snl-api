package uk.gov.hmcts.reform.sandl.snlapi.security.responses;

import lombok.Data;

@Data
public class ApiResponse {
    private Boolean success;
    private String message;
}
