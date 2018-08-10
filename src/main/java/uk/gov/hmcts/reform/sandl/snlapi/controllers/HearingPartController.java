package uk.gov.hmcts.reform.sandl.snlapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
}

