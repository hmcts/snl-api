package uk.gov.hmcts.reform.sandl.snlapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.sandl.snlapi.services.EventsCommunicationService;

@RestController
@RequestMapping("/poc")
public class PoCHelperController {

    @Autowired
    private EventsCommunicationService eventsCommunicationService;

    @PostMapping(path = "/loaddb", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String loadDb(@RequestBody String hearingPart) {
        return eventsCommunicationService.makeCall("/poc", HttpMethod.POST).getBody();
    }
}

