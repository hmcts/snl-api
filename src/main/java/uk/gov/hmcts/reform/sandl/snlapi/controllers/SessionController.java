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
@RequestMapping("/sessions")
public class SessionController {

    @Autowired
    private EventsCommunicationService eventsCommunicationService;

    @GetMapping(path = "", params = "date", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getSessions(@RequestParam("date") String date) {
        return eventsCommunicationService.makeCall("/sessions?date={date}", HttpMethod.GET, date).getBody();
    }

    @GetMapping(path = "", params = {"startDate", "endDate"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getSessions(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) {
        return eventsCommunicationService.makeCall("/sessions?startDate={startDate}&endDate={endDate}",
            HttpMethod.GET, startDate, endDate
        ).getBody();
    }

    @GetMapping(path = "/judge-diary", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getJudgeDiary(
        @RequestParam("judge") String judgeUsername,
        @RequestParam("startDate") String startDate,
        @RequestParam("endDate") String endDate) {
        return eventsCommunicationService.makeCall(
            "/sessions/judge-diary?judge={judgeUsername}&startDate={startDate}&endDate={endDate}",
            HttpMethod.GET, judgeUsername, startDate, endDate).getBody();
    }
}
