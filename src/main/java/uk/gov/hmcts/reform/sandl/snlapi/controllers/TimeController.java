package uk.gov.hmcts.reform.sandl.snlapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.sandl.snlapi.services.EventsCommunicationService;

@RestController
public class TimeController {

    @Autowired
    EventsCommunicationService eventsCommunicationService;

    @PutMapping(produces = "application/json")
    public ResponseEntity insertTime(@RequestBody String time) {
        eventsCommunicationService.makePutCall("/time", time);

        return new ResponseEntity<>(HttpStatus.OK.toString(), HttpStatus.OK);

    }
}
