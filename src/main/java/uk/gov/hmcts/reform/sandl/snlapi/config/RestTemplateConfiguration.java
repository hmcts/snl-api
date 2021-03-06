package uk.gov.hmcts.reform.sandl.snlapi.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import uk.gov.hmcts.reform.sandl.snlapi.exceptions.BeanValidationException;
import uk.gov.hmcts.reform.sandl.snlapi.exceptions.OptimisticLockException;

import java.io.IOException;
import java.util.Scanner;

@Configuration
public class RestTemplateConfiguration {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.errorHandler(
            new DefaultResponseErrorHandler() { //Handler should be excluded to a separate class to easier testing
                @Override
                public void handleError(ClientHttpResponse response) throws IOException {
                    if (response.getStatusCode() == HttpStatus.CONFLICT) {
                        throw new OptimisticLockException();
                    } else if (response.getStatusCode() == HttpStatus.BAD_REQUEST) {
                        Scanner s = new Scanner(response.getBody()).useDelimiter("\\A");
                        String result = s.hasNext() ? s.next() : "";
                        throw new BeanValidationException(result);
                    } else {
                        super.handleError(response);
                    }
                }
            }
        ).build();
    }
}
