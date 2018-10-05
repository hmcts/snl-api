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
    public String getProblems(@RequestParam("page") Integer page, @RequestParam("size") Integer size) {
        return eventsCommunicationService.makeCall("/problems?page=" + page + "&size="+ size, HttpMethod.GET).getBody();
    }


    @GetMapping(path = "by-entity-id", params = "id", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    String getProblemsByReferenceEntityId(@RequestParam("id") String id) {
        return eventsCommunicationService.makeCall("/problems/by-entity-id?id={id}",
            HttpMethod.GET, id).getBody();
    }

    @GetMapping(path = "by-user-transaction-id", params = "id", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    String getProblemsByUserTransactionId(@RequestParam("id") String id) {
        return eventsCommunicationService.makeCall("/problems/by-user-transaction-id?id={id}",
            HttpMethod.GET, id).getBody();
    }
}
