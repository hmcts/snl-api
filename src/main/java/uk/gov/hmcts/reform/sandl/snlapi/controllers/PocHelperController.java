package uk.gov.hmcts.reform.sandl.snlapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.sandl.snlapi.services.EventsCommunicationService;

@RestController
@RequestMapping("/poc")
public class PocHelperController {

    @Autowired
    private EventsCommunicationService eventsCommunicationService;

    @PostMapping(path = "/loaddb",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity loadDb() {
        eventsCommunicationService.makeCall("/poc", HttpMethod.POST);

        return new ResponseEntity<>(HttpStatus.OK.toString(), HttpStatus.OK);
    }
}

