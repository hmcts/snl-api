package uk.gov.hmcts.reform.sandl.snlapi.realevents.controllers;

import io.restassured.http.Header;
import org.junit.Test;
import uk.gov.hmcts.reform.sandl.snlapi.BaseIntegrationTest;

import static io.restassured.RestAssured.given;

public class RoomControllerIntRealTest extends BaseIntegrationTest {

    @Test
    public void rooms_shouldCallProperEventsEndpointAndReturnRooms() throws Exception {
        Header authenticationHeader = loginHelper.signInAsOfficer();

        given()
            .contentType("application/json")
            .header(authenticationHeader)
        .when()
            .get("/room")
        .then()
            .assertThat()
            .statusCode(200);
    }
}
