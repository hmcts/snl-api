package uk.gov.hmcts.reform.sandl.snlapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BeanValidationException extends RuntimeException {
    public BeanValidationException(String msg) {
        super(msg);
    }
}
