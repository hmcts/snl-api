package uk.gov.hmcts.reform.sandl.snlapi.services;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uk.gov.hmcts.reform.sandl.snlapi.security.services.S2SAuthenticationService;

@Service
public class EventsCommunicationService {

    private final String eventsUrl;
    private final RestTemplate restTemplate;
    private final S2SAuthenticationService s2SAuthenticationService;

    @Autowired
    public EventsCommunicationService(
        S2SAuthenticationService s2SAuthenticationService,
        RestTemplate restTemplate,
        @Value("${communication.eventsUrl:http://localhost:8092}") String eventsUrl
    ) {
        this.s2SAuthenticationService = s2SAuthenticationService;
        this.restTemplate = restTemplate;
        this.eventsUrl = eventsUrl;
    }

    @HystrixCommand
    public ResponseEntity<String> makeCall(String endpointWithParams, HttpMethod httpMethod, String... params) {
        HttpHeaders headers = this.s2SAuthenticationService.createAuthenticationHeader();
        HttpEntity headersOnlyEntity = new HttpEntity(headers);
        return restTemplate.exchange(
            eventsUrl + endpointWithParams, httpMethod, headersOnlyEntity, String.class, params
        );
    }

    public ResponseEntity<String> makePutCall(String endpointWithParams, String body, String... params) {
        return makeCallWithBody(endpointWithParams, body, HttpMethod.PUT, params);
    }

    public ResponseEntity<String> makePostCall(String endpointWithParams, String body, String... params) {
        return makeCallWithBody(endpointWithParams, body, HttpMethod.POST, params);
    }

    public ResponseEntity<String> makeDeleteCall(String endpointWithParams, String... params) {
        return makeCall(endpointWithParams, HttpMethod.DELETE, params);
    }

    @HystrixCommand
    protected ResponseEntity<String> makeCallWithBody(String endpointWithParams,
                                                      String body,
                                                      HttpMethod method,
                                                      String... params) {
        HttpHeaders headers = this.s2SAuthenticationService.createAuthenticationHeader();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        return restTemplate.exchange(eventsUrl + endpointWithParams, method, entity, String.class, params);
    }
}
