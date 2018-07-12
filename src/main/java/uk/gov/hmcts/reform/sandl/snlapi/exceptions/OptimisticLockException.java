package uk.gov.hmcts.reform.sandl.snlapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Should be thrown when there is an 'optimistic lock' related response from the database (or another service)
 * This class is used by spring internals to return proper http response
 * based on ResponseStatus annotation content.
 * <p>
 * Note: Typical {@code RuntimeException} constructors are omitted as this class should use
 * spring ResponseStatus annotation
 * </p>
 */
@ResponseStatus(value = HttpStatus.CONFLICT, reason = "The object version has changed")
public class OptimisticLockException extends RuntimeException {
    /**
     * Typical {@code RuntimeException} constructors are omitted as this class should use
     * spring ResponseStatus annotation.
     */
    public OptimisticLockException() {
        super();
    }
}
