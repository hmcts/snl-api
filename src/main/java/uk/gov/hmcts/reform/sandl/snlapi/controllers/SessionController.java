package uk.gov.hmcts.reform.sandl.snlapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.sandl.snlapi.services.EventsCommunicationService;

import java.io.IOException;
@RestController
public class SessionController {

    @Autowired
    private EventsCommunicationService eventsCommunicationService;

    @RequestMapping(path = "/sessions", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String getSessions(@RequestParam("date") String date) {
        return eventsCommunicationService.makeCall("/sessions?date={date}", HttpMethod.GET, date).getBody();
    }

    @CrossOrigin
    @ResponseBody
    @RequestMapping(path = "/sessions", method = RequestMethod.PUT, produces = "application/json")
    public String insertSession(@RequestBody String session) throws IOException {
        return eventsCommunicationService.makePutCall("/sessions", HttpMethod.PUT, session).getBody();
    }
}
