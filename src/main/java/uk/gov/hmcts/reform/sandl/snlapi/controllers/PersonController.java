package uk.gov.hmcts.reform.sandl.snlapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.sandl.snlapi.services.EventsCommunicationService;

@RestController
@RequestMapping("/person")
public class PersonController {

    @Autowired
    private EventsCommunicationService eventsCommunicationService;

    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getPersons() {
        return eventsCommunicationService.makeCall("/person", HttpMethod.GET).getBody();
    }

    @GetMapping(path = "", params = "personType", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getJudges(@RequestParam("personType") String personType) {
        return eventsCommunicationService.makeCall("/person?personType={personType}",
            HttpMethod.GET, personType).getBody();
    }
}
