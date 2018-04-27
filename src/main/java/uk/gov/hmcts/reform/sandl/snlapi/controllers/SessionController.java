package uk.gov.hmcts.reform.sandl.snlapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.sandl.snlapi.services.EventsCommunicationService;

@RestController
public class SessionController {

    @Autowired
    private EventsCommunicationService eventsCommunicationService;

    @RequestMapping(path = "/sessions", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String getSessions(@RequestParam("date") String date) {
        return eventsCommunicationService.makeCall("/sessions?date={date}", HttpMethod.GET, date).getBody();
    }
}
