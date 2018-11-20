package uk.gov.hmcts.reform.sandl.snlapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.sandl.snlapi.services.EventsCommunicationService;

import java.util.Optional;

@RestController
@RequestMapping("/sessions")
@Secured("ROLE_USER")
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

    @RequestMapping(path = "/search", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity searchSession(
        @RequestParam(value = "page", required = false) Optional<Integer> page,
        @RequestParam(value = "size", required = false) Optional<Integer> size,
        @RequestParam(value = "sort", required = false) Optional<String> sort,
        @RequestBody String searchCriteriaList) {
        String url = "/sessions/search";
        url += (page.isPresent() && size.isPresent()) ? "?page=" + page.get() + "&size=" + size.get() : "";

        if (sort.isPresent()) {
            url += url.contains("?") ? "&" : "?";

            String[] sortPair = sort.get().split(":");
            url += "sort" + sortPair[0] + ":" + sortPair[1];
        }

        return eventsCommunicationService.makePostCall(url, searchCriteriaList);
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getSessionById(@PathVariable("id") String id) {
        return eventsCommunicationService.makeCall("/sessions/{id}", HttpMethod.GET, id).getBody();
    }

    @RequestMapping(method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity insertSession(@RequestBody String session) {
        return eventsCommunicationService.makePutCall("/sessions", session);
    }

    @RequestMapping(path = "/update", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity updateSession(@RequestBody String session) {
        return eventsCommunicationService.makePutCall("/sessions/update", session);
    }

    @RequestMapping(path = "/amend", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity amendSession(@RequestBody String session) {
        return eventsCommunicationService.makePostCall("/sessions/amend", session);
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
