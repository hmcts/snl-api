package uk.gov.hmcts.reform.sandl.snlapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.sandl.snlapi.services.EventsCommunicationService;

import java.io.IOException;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/sessions")
public class SessionController {

    @Autowired
    private EventsCommunicationService eventsCommunicationService;

    @GetMapping(path = "", params = "date", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getSessions(@RequestParam("date") String date) {
        return eventsCommunicationService.makeCall("/sessions?date={date}", HttpMethod.GET, date).getBody();
    }

    @GetMapping(path = "", params = {"startDate", "endDate"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getSessions(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) {
        return eventsCommunicationService.makeCall("/sessions?startDate={startDate}&endDate={endDate}",
            HttpMethod.GET, startDate, endDate
        ).getBody();
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getSessionById(@PathVariable("id") String id) {
        return eventsCommunicationService.makeCall("/sessions/{id}", HttpMethod.GET, id).getBody();
    }

    @RequestMapping(method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity insertSession(@RequestBody String session) throws IOException {
        eventsCommunicationService.makePutCall("/sessions", session);
        return ok("{\"status\": \"Created\"}");
    }

    @GetMapping(path = "/judge-diary", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getJudgeDiary(
        @RequestParam("judge") String judgeUsername,
        @RequestParam("startDate") String startDate,
        @RequestParam("endDate") String endDate) {
        return eventsCommunicationService.makeCall(
            "/sessions/judge-diary?judge={judgeUsername}&startDate={startDate}&endDate={endDate}",
            HttpMethod.GET, judgeUsername, startDate, endDate).getBody();
    }
}
