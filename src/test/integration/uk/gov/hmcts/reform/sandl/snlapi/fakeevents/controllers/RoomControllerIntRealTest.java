package uk.gov.hmcts.reform.sandl.snlapi.fakeevents.controllers;

import io.restassured.http.Header;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import uk.gov.hmcts.reform.sandl.snlapi.fakeevents.BaseIntegrationTestWithFakeEvents;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static io.restassured.RestAssured.given;

public class RoomControllerIntRealTest extends BaseIntegrationTestWithFakeEvents {

    @Test
    public void rooms_shouldCallProperEventsEndpointAndReturnRooms() throws Exception {
        stubFor(get(urlEqualTo("/room"))
            .willReturn(aResponse()
                .withStatus(HttpStatus.OK.value())
                .withHeader("Content-Type", "application/json")
                .withBody("[]")));

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
