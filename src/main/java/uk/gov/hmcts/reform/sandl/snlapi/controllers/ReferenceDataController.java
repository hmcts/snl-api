package uk.gov.hmcts.reform.sandl.snlapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.sandl.snlapi.services.EventsCommunicationService;

@RestController()
@RequestMapping("/reference")
public class ReferenceDataController {

    @Autowired
    private EventsCommunicationService eventsCommunicationService;

    @GetMapping(path = "/case-types", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getCaseTypes() {
        String url = "/reference/case-types";
        return eventsCommunicationService.makeCall(url, HttpMethod.GET).getBody();
    }

    @GetMapping(path = "/session-types", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getSessionTypes() {
        String url = "/reference/session-types";
        return eventsCommunicationService.makeCall(url, HttpMethod.GET).getBody();
    }


    @GetMapping(path = "/hearing-types", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getHearingTypes() {
        String url = "/reference/hearing-types";
        return eventsCommunicationService.makeCall(url, HttpMethod.GET).getBody();
    }


    @GetMapping(path = "/room-types", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getRoomTypes() {
        String url = "/reference/room-types";
        return eventsCommunicationService.makeCall(url, HttpMethod.GET).getBody();
    }

}
