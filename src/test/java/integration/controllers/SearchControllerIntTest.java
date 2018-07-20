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

public class SearchControllerIntTest extends BaseIntegrationTestWithFakeEvents {

    @Test
    public void search_shouldCallProperEventsEndpointAndReturnResults() throws Exception {
        stubFor(get(urlEqualTo("/search?from=1&to=2&durationInSeconds=3"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("[\"results\"]")));

        Header authenticationHeader = signIn("officer1", "asd");

        given()
            .contentType("application/json")
            .header(authenticationHeader)
            .when()
            .get("/search?from=1&to=2&durationInSeconds=3")
            .then()
            .assertThat()
            .statusCode(200)
            .and().extract().response();

        verify(getRequestedFor(urlEqualTo("/search?from=1&to=2&durationInSeconds=3")));
    }
}
