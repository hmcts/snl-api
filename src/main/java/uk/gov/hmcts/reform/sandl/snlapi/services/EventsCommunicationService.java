package uk.gov.hmcts.reform.sandl.snlapi.services;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Value("${communication.eventsUrl:http://localhost:8092}")
    private String eventsUrl;

    @Autowired
    private RestTemplate restTemplate;

    public ResponseEntity<String> makeCall(String endpointWithParams, HttpMethod httpMethod, String... params) {
        return restTemplate.exchange(
            eventsUrl + endpointWithParams, httpMethod, null, String.class, params
        );
    }

    public ResponseEntity<String> makePutCall(String endpointWithParams, String body, String... params) {
        return makeCallWithBody(endpointWithParams, body, HttpMethod.PUT, params);
    }

    public ResponseEntity<String> makePostCall(String endpointWithParams, String body, String... params) {
        return makeCallWithBody(endpointWithParams, body, HttpMethod.POST, params);
    }

    protected ResponseEntity<String> makeCallWithBody(String endpointWithParams, String body, HttpMethod method, String... params) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        return restTemplate.exchange(eventsUrl + endpointWithParams, method, entity, String.class, params);
    }
}
