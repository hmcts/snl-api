package uk.gov.hmcts.reform.sandl.snlapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.sandl.snlapi.services.EventsCommunicationService;

@RestController
@RequestMapping("/status-config")
public class StatusConfigController {
    @Autowired
    private EventsCommunicationService eventsCommunicationService;

    @GetMapping(path = "", produces =  MediaType.APPLICATION_JSON_VALUE)
    public String getAllStatuses() {
        return eventsCommunicationService.makeCall("/status-config", HttpMethod.GET).getBody();
    }
}
