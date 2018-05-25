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
@RequestMapping("/problems")
public class ProblemController {

    @Autowired
    EventsCommunicationService eventsCommunicationService;

    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getProblems() {
        return eventsCommunicationService.makeCall("/problems", HttpMethod.GET).getBody();
    }


    @GetMapping(path = "by-reference-type-id", params = "id", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    String getProblemsByReferenceTypeId(@RequestParam("id") String id) {
        return eventsCommunicationService.makeCall("/problems/by-reference-type-id?id={id}",
            HttpMethod.GET, id).getBody();
    }

}
