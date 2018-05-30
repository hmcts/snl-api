package uk.gov.hmcts.reform.sandl.snlapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.sandl.snlapi.services.EventsCommunicationService;

@RestController
@RequestMapping("/user-transaction")
public class UserTransactionController {

    @Autowired
    private EventsCommunicationService eventsCommunicationService;

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getUserTransactionById(@PathVariable("id") String id) {
        return eventsCommunicationService.makeCall("/usertransactions/{id}", HttpMethod.GET, id).getBody();
    }
}
