package uk.gov.hmcts.reform.sandl.snlapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
        return eventsCommunicationService.makeCall("/user-transaction/{id}", HttpMethod.GET, id).getBody();
    }

    @PostMapping(path = "/{id}/commit", produces = MediaType.APPLICATION_JSON_VALUE)
    public String commitTransaction(@PathVariable("id") String id) {
        return eventsCommunicationService.makePostCall("/user-transaction/{id}/commit",null, id).getBody();
    }

    @PostMapping(path = "/{id}/rollback", produces = MediaType.APPLICATION_JSON_VALUE)
    public String rollbackTransction(@PathVariable("id") String id) {
        return eventsCommunicationService.makePostCall("/user-transaction/{id}/rollback",null, id).getBody();
    }
}
