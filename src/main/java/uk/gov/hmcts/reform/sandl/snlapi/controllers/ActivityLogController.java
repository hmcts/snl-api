package uk.gov.hmcts.reform.sandl.snlapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.sandl.snlapi.services.EventsCommunicationService;

import java.util.UUID;

@RestController
@RequestMapping("activity-log")
public class ActivityLogController {

    @Autowired
    EventsCommunicationService eventsCommunicationService;

    @GetMapping(path = "/{entityId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getActivitiesByEntityId(@PathVariable("entityId") UUID entityId) {
        return eventsCommunicationService.makeCall("/activity-log/{entityId}",
            HttpMethod.GET, entityId.toString()).getBody();
    }
}
