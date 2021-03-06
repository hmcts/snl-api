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
@RequestMapping("/hearing-part")
public class HearingPartController {

    @Autowired
    private EventsCommunicationService eventsCommunicationService;

    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getHearingParts(@RequestParam("isListed") Optional<Boolean> isListed) {
        String url = "/hearing-part" +  (isListed.isPresent() ? "?isListed=" + isListed.get() : "");
        return eventsCommunicationService.makeCall(url, HttpMethod.GET).getBody();
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getHearingPartById(@PathVariable("id") String id) {
        return eventsCommunicationService.makeCall("/hearing-part/{id}", HttpMethod.GET, id).getBody();
    }

    @PutMapping(path = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity upsertHearingPart(@RequestBody String hearingPart) {
        return eventsCommunicationService.makePutCall("/hearing-part", hearingPart);
    }

    @PutMapping(path = "{hearingPartId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity assignHearingPartToSession(@PathVariable String hearingPartId,
                                                     @RequestBody String assignment) {
        return eventsCommunicationService.makePutCall("/hearing-part/{hearingPartId}",
            assignment, hearingPartId);
    }

    @PutMapping(path = "/amend-scheduled-listing", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity amendScheduledListing(@RequestBody String amend) {
        return eventsCommunicationService.makePutCall(
            "/hearing-part/amend-scheduled-listing",
            amend);
    }

    @PostMapping(path = "delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity deleteHearingPart(@RequestBody String deleteHearingPart) {
        return eventsCommunicationService.makePostCall("/hearing-part/delete", deleteHearingPart);
    }

    @PutMapping(path = "create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createHearingPartAction(@RequestBody String createHearingPart) {
        return eventsCommunicationService.makePutCall("/hearing-part/create", createHearingPart);
    }

    @PutMapping(path = "update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateHearingPartAction(@RequestBody String updateHearingPart) {
        return eventsCommunicationService.makePutCall("/hearing-part/update", updateHearingPart);
    }
}
