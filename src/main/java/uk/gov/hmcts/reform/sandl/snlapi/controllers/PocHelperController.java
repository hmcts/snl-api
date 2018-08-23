package uk.gov.hmcts.reform.sandl.snlapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import uk.gov.hmcts.reform.sandl.snlapi.security.services.S2SAuthenticationService;

@RestController
@RequestMapping("/poc")
/*
This controller is only a temporary solution that allows loading data from db into rules engine.
One the final solution is in place of restoring state of drools then this controller together
with the ui should be removed.
 */
public class PocHelperController {

    private final String eventsUrl;
    private final RestTemplate restTemplate;
    private final S2SAuthenticationService s2SAuthenticationService;

    @Autowired
    public PocHelperController(
        S2SAuthenticationService s2SAuthenticationService,
        RestTemplate restTemplate,
        @Value("${communication.eventsUrl:http://localhost:8092}") String eventsUrl
    ) {
        this.s2SAuthenticationService = s2SAuthenticationService;
        this.restTemplate = restTemplate;
        this.eventsUrl = eventsUrl;
    }

    /*
    This method intentionally does not use hystrix so it does not reach the timeout and may be long running.
     */
    @PostMapping(path = "/loaddb",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity loadDb() {

        HttpHeaders headers = this.s2SAuthenticationService.createAuthenticationHeader();
        HttpEntity headersOnlyEntity = new HttpEntity(headers);
        restTemplate.exchange(
            eventsUrl + "/poc", HttpMethod.POST, headersOnlyEntity, String.class
        );

        return new ResponseEntity<>(HttpStatus.OK.toString(), HttpStatus.OK);
    }
}

