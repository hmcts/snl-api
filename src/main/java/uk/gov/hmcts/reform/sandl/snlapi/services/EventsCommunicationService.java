package uk.gov.hmcts.reform.sandl.snlapi.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventsCommunicationService {

    private static final RestTemplate REST_TEMPLATE = new RestTemplate();

    @Value("${communication.eventsUrl:http://localhost:8092}")
    private String eventsUrl;

    public ResponseEntity<String> makeCall(String endpointWithParams, HttpMethod httpMethod, String... params) {
        return REST_TEMPLATE.exchange(
            eventsUrl + endpointWithParams, httpMethod, null, String.class, new Object[]{params}
        );
    }

    public ResponseEntity<String> makePutCall(String endpointWithParams, HttpMethod httpMethod, String params) {

        List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
        acceptableMediaTypes.add(MediaType.APPLICATION_JSON); // Set what you need

        // Prepare header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(acceptableMediaTypes);
        HttpEntity<String> entity = new HttpEntity<String>(params, headers);


        return REST_TEMPLATE.exchange(
            eventsUrl + endpointWithParams, httpMethod, entity, String.class
        );
    }
}
