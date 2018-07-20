package integration.controllers;

import integration.BaseIntegrationTestWithFakeEvents;
import io.restassured.http.Header;
import org.junit.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static io.restassured.RestAssured.given;

public class RoomControllerIntTest extends BaseIntegrationTestWithFakeEvents {

    @Test
    public void rooms_shouldCallProperEventsEndpointAndReturnRooms() throws Exception {
        stubFor(get(urlEqualTo("/room"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("[\"rooms\"]")));

        Header authenticationHeader = signIn("officer1", "asd");

        given()
            .contentType("application/json")
            .header(authenticationHeader)
            .when()
            .get("/room")
            .then()
            .assertThat()
            .statusCode(200)
            .and().extract().response();

        verify(getRequestedFor(urlEqualTo("/room")));
    }
}
