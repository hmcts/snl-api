package uk.gov.hmcts.reform.sandl.snlapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uk.gov.hmcts.reform.sandl.snlapi.services.EventsCommunicationService;

@Controller
public class SearchController {

    @Autowired
    private EventsCommunicationService eventsCommunicationService;

    @GetMapping(path = "/search")
    public ResponseEntity<String> searchPossibleSessions(
        @RequestParam(value = "from") String from,
        @RequestParam(value = "to") String to,
        @RequestParam(value = "durationInSeconds") int duration,
        @RequestParam(value = "judge", required = false) String judgeId,
        @RequestParam(value = "room", required = false) String roomId) {

        String url = "/search" + buildParams(from, to, duration, judgeId, roomId);
        return eventsCommunicationService.makeCall(url,
            HttpMethod.GET, from, to, String.valueOf(duration), judgeId, roomId
        );
    }

    private String buildParams(String from, String to, int duration, String judgeId, String roomId) {
        String params = String.format("?from=%s&to=%s&durationInSeconds=%s", from, to, duration);

        if (judgeId != null) {
            params += String.format("&judge=%s", judgeId);
        }

        if (roomId != null) {
            params += String.format("&room=%s", roomId);
        }
        return params;
    }
}
