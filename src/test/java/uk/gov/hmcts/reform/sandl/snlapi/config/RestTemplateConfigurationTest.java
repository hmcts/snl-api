package uk.gov.hmcts.reform.sandl.snlapi.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.ResponseErrorHandler;
import uk.gov.hmcts.reform.sandl.snlapi.exceptions.BeanValidationException;
import uk.gov.hmcts.reform.sandl.snlapi.exceptions.OptimisticLockException;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class RestTemplateConfigurationTest {

    @Mock
    ClientHttpResponse response;

    @Test(expected = OptimisticLockException.class)
    public void handleError_ThrowsExceptionOnConflict() throws IOException {
        when(response.getStatusCode()).thenReturn(HttpStatus.CONFLICT);

        ResponseErrorHandler handler = new RestTemplateConfiguration()
            .restTemplate(new RestTemplateBuilder())
            .getErrorHandler();

        handler.handleError(response);
    }

    @Test(expected = BeanValidationException.class)
    public void handleError_ThrowsExceptionOnBadRequest() throws IOException {
        when(response.getStatusCode()).thenReturn(HttpStatus.BAD_REQUEST);
        String validationErrorString = "{\"errorDetailsList\":[{\"field\":\"duration\","
            + "\"errorMessage\":\"Duration is shorter than 1 minutes\"}]}";
        when(response.getBody()).thenReturn(new ByteArrayInputStream(validationErrorString.getBytes()));

        ResponseErrorHandler handler = new RestTemplateConfiguration()
            .restTemplate(new RestTemplateBuilder())
            .getErrorHandler();

        handler.handleError(response);
    }
}
