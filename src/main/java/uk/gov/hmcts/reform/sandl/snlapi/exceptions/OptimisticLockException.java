package uk.gov.hmcts.reform.sandl.snlapi.exceptions;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "The object version has changed")
@NoArgsConstructor
public class OptimisticLockException extends RuntimeException {
}
