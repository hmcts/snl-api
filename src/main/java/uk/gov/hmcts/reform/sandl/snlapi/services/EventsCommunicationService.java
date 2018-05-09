package uk.gov.hmcts.reform.sandl.snlapi.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class EventsCommunicationService {

    private static final RestTemplate REST_TEMPLATE = new RestTemplate();

    @Value("${communication.eventsUrl:http://localhost:8092}")
    private String eventsUrl;

    public ResponseEntity<String> makeCall(String endpointWithParams, HttpMethod httpMethod, String... params) {
        return REST_TEMPLATE.exchange(
            eventsUrl + endpointWithParams, httpMethod, null, String.class, params
        );
    }

    public ResponseEntity<String> makePutCall(String endpointWithParams, String params) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(params, headers);

        return REST_TEMPLATE.exchange(eventsUrl + endpointWithParams, HttpMethod.PUT, entity, String.class);
    }
}
