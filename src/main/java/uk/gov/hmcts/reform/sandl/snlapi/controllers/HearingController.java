package uk.gov.hmcts.reform.sandl.snlapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.hmcts.reform.sandl.snlapi.services.EventsCommunicationService;

import java.util.Optional;

@RestController
@RequestMapping("hearing")
public class HearingController {

    @Autowired
    private EventsCommunicationService eventsCommunicationService;

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getHearingById(@PathVariable("id") String id) {
        return eventsCommunicationService.makeCall("/hearing/{id}", HttpMethod.GET, id).getBody();
    }

    @GetMapping(path = "/{id}/with-sessions", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getHearingByIdWithSessions(@PathVariable("id") String id) {
        return eventsCommunicationService.makeCall("/hearing/{id}/with-sessions", HttpMethod.GET, id).getBody();
    }

    @GetMapping(path = "/{id}/for-amendment", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getHearingByIdForAmendment(@PathVariable("id") String id) {
        return eventsCommunicationService.makeCall("/hearing/{id}/for-amendment", HttpMethod.GET, id).getBody();
    }

    @PutMapping(path = "/{hearingId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity assignHearingPartToSession(
        @PathVariable String hearingId,
        @RequestBody String assignment) {
        return eventsCommunicationService.makePutCall("/hearing/{hearingId}", assignment, hearingId);
    }

    @PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String searchHearings(@RequestParam(value = "page", required = false) Optional<Integer> page,
                                 @RequestParam(value = "size", required = false) Optional<Integer> size,
                                 @RequestBody String searchCriteria) {

        String url = "/hearing";
        url += (page.isPresent() && size.isPresent()) ? "?page=" + page.get() + "&size=" + size.get() : "";

        return eventsCommunicationService.makePostCall(url, searchCriteria).getBody();
    }

    @GetMapping(path = "/for-listing", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String searchHearingsForListing(@RequestParam(value = "page", required = false) Optional<Integer> page,
                         @RequestParam(value = "size", required = false) Optional<Integer> size,
                         @RequestParam(value = "sortByProperty", required = false) Optional<String> sortByProperty,
                         @RequestParam(value = "sortByDirection", required = false) Optional<String> sortByDirection) {

        String url = "/hearing/for-listing";
        url += (page.isPresent() && size.isPresent()) ? "?page=" + page.get() + "&size=" + size.get() : "";
        url += (sortByDirection.isPresent() && sortByProperty.isPresent()) ?
            "&sortByDirection=" + sortByDirection.get() + "&sortByProperty=" + sortByProperty.get() : "";

        return eventsCommunicationService.makeCall(url, HttpMethod.GET).getBody();
    }

    @PutMapping(path = "/unlist", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity unlistHearingAction(@RequestBody String unlistHearingRequest) {
        return eventsCommunicationService.makePutCall("/hearing/unlist", unlistHearingRequest);
    }

    @PutMapping(path = "/adjourn", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity adjournHearingAction(@RequestBody String adjournHearingRequest) {
        return eventsCommunicationService.makePutCall("/hearing/adjourn", adjournHearingRequest);
    }

    @PutMapping(path = "/withdraw", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity withdrawHearingAction(@RequestBody String withdrawHearingRequest) {
        return eventsCommunicationService.makePutCall("/hearing/withdraw", withdrawHearingRequest);
    }
}
