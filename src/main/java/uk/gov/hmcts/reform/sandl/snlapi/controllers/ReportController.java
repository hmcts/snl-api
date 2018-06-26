package uk.gov.hmcts.reform.sandl.snlapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.sandl.snlapi.services.EventsCommunicationService;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private EventsCommunicationService eventsCommunicationService;

    @GetMapping(path = "unlisted-hearing-requests", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getUnlistedHearingRequests() {
        return eventsCommunicationService.makeCall("/report/unlisted-hearing-requests",
            HttpMethod.GET).getBody();
    }
}
