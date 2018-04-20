package uk.gov.hmcts.reform.sandl.snlapi.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

/**
 * Default endpoints per application.
 */
@RestController
public class RootController {

    /**
     * Root GET endpoint.
     *
     * <p>Azure application service has a hidden feature of making requests to root endpoint when
     * "Always On" is turned on.
     * This is the endpoint to deal with that and therefore silence the unnecessary 404s as a response code.
     *
     * @return Welcome message from the service.
     */
    @GetMapping
    public ResponseEntity<String> welcome() {
        return ok("Welcome to snl-api");
    }

    @CrossOrigin
    @RequestMapping(path = "/get-sessions", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String getSessions(@RequestParam("date") String date) {
        if (date.equals("15-04-2018")) {
            return "[{\"date\": \"Wed Apr 11 2018 14:12:36 GMT+0100 (BST)\",\"room\": {\"name\": \"Room one\"},"
                + "\"judge\": {\"name\":\"John Judge\"}, \"durationInMinutes\": 90, \"jurisdiction\":\"ABC\"},"
                + "{\"date\": \"Wed Apr 11 2018 14:12:36 GMT+0100 (BST)\",\"room\": {\"name\": \"Room one\"},"
                + "\"judge\":{\"name\":\"John Judge\"}, \"durationInMinutes\": 90, \"jurisdiction\":\"ABC\"}]";
        } else {
            return "[{\"date\": \"Wed Apr 11 2018 14:12:36 GMT+0100 (BST)\",\"room\": {\"name\": \"Room one\"},"
                + "\"judge\":{\"name\":\"John Judge\"}, \"durationInMinutes\": 90, \"jurisdiction\":\"ABC\"}]";
        }
    }
}
