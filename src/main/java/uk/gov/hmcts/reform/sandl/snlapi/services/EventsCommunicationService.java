package uk.gov.hmcts.reform.sandl.snlapi.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import uk.gov.hmcts.reform.sandl.snlapi.exceptions.OptimisticLockException;

import java.io.IOException;

@Service
public class EventsCommunicationService {

    private static final RestTemplate REST_TEMPLATE = new RestTemplate();

    @Value("${communication.eventsUrl:http://localhost:8092}")
    private String eventsUrl;

    EventsCommunicationService() {
        EventsCommunicationService.REST_TEMPLATE.setErrorHandler(
            new DefaultResponseErrorHandler() {
                @Override
                public void handleError(ClientHttpResponse response) throws IOException {
                    if (response.getStatusCode() == HttpStatus.CONFLICT) {
                        throw new OptimisticLockException();
                    } else {
                        super.handleError(response);
                    }
                }
            }
        );
    }

    public ResponseEntity<String> makeCall(String endpointWithParams, HttpMethod httpMethod, String... params) {
        return REST_TEMPLATE.exchange(
            eventsUrl + endpointWithParams, httpMethod, null, String.class, params
        );
    }

    public ResponseEntity<String> makePutCall(String endpointWithParams, String body, String... params) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        return REST_TEMPLATE.exchange(eventsUrl + endpointWithParams, HttpMethod.PUT, entity, String.class, params);
    }

    public ResponseEntity<String> makePostCall(String endpointWithParams, String body, String... params) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        return REST_TEMPLATE.exchange(eventsUrl + endpointWithParams, HttpMethod.POST, entity, String.class, params);
    }
}
